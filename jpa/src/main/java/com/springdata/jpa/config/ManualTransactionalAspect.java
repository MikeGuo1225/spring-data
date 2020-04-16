package com.springdata.jpa.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 手动提交事物
 */
@Aspect
@Component
public class ManualTransactionalAspect {
    private static org.slf4j.Logger log = LoggerFactory.getLogger(ManualTransactionalAspect.class);

    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 启动目标方法执行，通常设置代理。JoinPoint通常走自己的逻辑
     * @param proceedingJoinPoint
     */
    @Around("@annotation(com.springdata.jpa.config.annotation.ManualTransactional) && execution(public * *(..))")
    public void manualTransactionAspect(final ProceedingJoinPoint proceedingJoinPoint) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);

        try {
            Object[] args = proceedingJoinPoint.getArgs();
            if (args != null && args.length > 0) {
                proceedingJoinPoint.proceed(args);
            }
            transactionManager.commit(transactionStatus);
        } catch (Throwable e) {
            log.error("Manual transaction has an error: {}", e.getMessage(), e);
            transactionManager.rollback(transactionStatus);
        }
    }
}
