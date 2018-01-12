package com.springdata.jpa.repository;

import com.springdata.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by dongyaofeng on 2017/12/25.
 */
public interface UserRepostitory extends JpaRepository<User, Integer>, /*用户多条件查询*/ JpaSpecificationExecutor<User> {

    /*
     * Like 	        findByNameLike 	        ...  where x.name like ?1
     * NotLike 	        findByNameNotLike 	    ...  where x.name not like ?1
     * StartingWith 	findByNameStartingWith 	...  where x.name like ?1(parameter bound with appended %)
     * EndingWith 	    findByNameEndingWith 	...  where x.name like ?1(parameter bound with prepended %)
     * Containing 	    findByNameContaining 	...  where x.name like ?1(parameter bound wrapped in %)
     * OrderBy 	        findByAgeOrderByName 	...  where x.age = ?1 order by x.name desc
     * Not 	            findByNameNot 	        ...  where x.name <> ?1
     * In 	            findByAgeIn 	        ...  where x.age in ?1
     * NotIn 	        findByAgeNotIn 	        ...  where x.age not in ?1
     * True 	        findByActiveTrue 	    ...  where x.avtive = true
     * Flase 	        findByActiveFalse 	    ...  where x.active = false
     * And  	        findByNameAndAge 	    ...  where x.name = ?1 and x.age = ?2
     * Or 	            findByNameOrAge 	    ...  where x.name = ?1 or x.age = ?2
     * Between 	        findBtAgeBetween 	    ...  where x.age between ?1 and ?2
     * LessThan 	    findByAgeLessThan 	    ...  where x.age  <  ?1
     * GreaterThan 	    findByAgeGreaterThan 	...  where x.age > ?1
     * IsNull 	        findByAgeIsNull 	    ...  where x.age is null
     */


    // 通过ID查询
    User findById(Integer id);

    // 用户名查询
    List<User> findByUsername(String username);

    //修改 (需要  @Modifying  配合 ,必须要有 事物, 不然报错)
    @Transactional //为方便起见,事物暂时写在 dao 层
    @Modifying //注解完成修改操作（注意：不支持新增）
    @Query(value = "update User  u set  u.age=?2 where u.id =?1")
    void updateAge(Integer id, Integer age);

}
