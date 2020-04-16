package com.springdata.jpa.config;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.springdata.jpa.interceptor.OkHttpClientInterceptor;
import com.springdata.jpa.serializer.CustomDateDeserializer;
import com.springdata.jpa.serializer.CustomDateSerializer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.okhttp3.OkHttpMetricsEventListener;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import retrofit2.Invocation;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Configuration
public class RetrofitConfig {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new CustomDateDeserializer())
            .registerTypeAdapter(Date.class, new CustomDateSerializer())
            .create();

    private static final ImmutableSet retrofitHttpMethodAnnotationName;

    static {
        retrofitHttpMethodAnnotationName = ImmutableSet.of(
                GET.class.getName(),
                POST.class.getName(),
                PUT.class.getName(),
                DELETE.class.getName(),
                PATCH.class.getName(),
                HEAD.class.getName(),
                OPTIONS.class.getName()
        );
    }

    @Value("${host.service}")
    private String baseUrl;

    @Autowired
    protected HttpServletRequest request;

    private MeterRegistry meterRegistry = new SimpleMeterRegistry();

    private ThreadLocal<Retrofit> retrofit = new ThreadLocal<Retrofit>() {

        private OkHttpClient newHttpClient() {
            OkHttpMetricsEventListener metricsEventListener = OkHttpMetricsEventListener
                    .builder(meterRegistry, "http_client_requests")
                    .uriMapper(request -> {
                        Invocation invocation = request.tag(Invocation.class);

                        if (invocation != null) {
                            for (Annotation declaredAnnotation : invocation.method().getDeclaredAnnotations()) {
                                String name = declaredAnnotation.annotationType().getName();
                                if (retrofitHttpMethodAnnotationName.contains(name)) {
                                    try {
                                        return MethodUtils.invokeMethod(declaredAnnotation, true, "value").toString();
                                    } catch (Exception ignored) { }
                                    break;
                                }
                            }
                        }
                        // OkHttpMetricsEventListener returns 'none' internally, so this keeps things consistent.
                        return "none";
                    })
                    .build();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new OkHttpClientInterceptor(request));
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(2, TimeUnit.MINUTES);
            builder.writeTimeout(2, TimeUnit.MINUTES);
            builder.eventListener(metricsEventListener);
            return builder.build();
        }

        @Override
        protected Retrofit initialValue() {
            return new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(newHttpClient())
                    .build();
        }
    };

    //实现每个接口的@Bean
}
