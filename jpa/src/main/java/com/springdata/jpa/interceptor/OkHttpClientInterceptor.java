package com.springdata.jpa.interceptor;

import com.springdata.jpa.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class OkHttpClientInterceptor implements Interceptor {

    HttpServletRequest request;

    public OkHttpClientInterceptor(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Builder builder = chain.request().newBuilder()
            .header("Cache-Control", "no-cache")
            .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
            .header("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (StringUtils.isNotBlank(language)) {
            builder.header(HttpHeaders.ACCEPT_LANGUAGE, language);
        }
        String auth = request.getHeader(CommonConstants.HEADER_AUTH);
        if (StringUtils.isNotBlank(auth)) {
            builder.header(CommonConstants.HEADER_AUTH, auth);
        }
        Request okRequest = builder.build();
        String url = okRequest.url().toString();
        boolean failed = false;
        if (url.contains("&password")) {
            url = url.replaceAll("&password=.*&grant", "&password=&grant");
        }
        StopWatch watch = new StopWatch();
        watch.start();
        try {
            return chain.proceed(okRequest);
        } catch (Exception e) {
            watch.stop();
            failed = true;
            log.error("{} {}, Authorization: {}, {}, Cost: {}ms.", okRequest.method(), url, auth, e.getMessage(), watch.getTotalTimeMillis());
            throw e;
        } finally {
            if (!failed) {
                watch.stop();
                log.info("{} {}, Authorization: {}, Cost: {}ms.", okRequest.method(), url, auth, watch.getTotalTimeMillis());
            }
        }
    }

}
