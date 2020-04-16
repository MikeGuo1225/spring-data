package com.springdata.jpa;

import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.ErrorPageFilter;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@SpringBootApplication
@EnableSwagger2
public class JpaApplication extends SpringBootServletInitializer {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(JpaApplication.class);

	public static final String SPRING_PROFILE_LOCAL = "local";

	@Inject
	private Environment env;

	/**
	 * Main method, used to run the application.
	 *
	 */
	public static void main(String[] args) throws UnknownHostException {
		SpringApplication app = new SpringApplication(JpaApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
		app.getSources().remove( ErrorPageFilter.class );
		Environment env = app.run(args).getEnvironment();
		log.info("Access URLs:\n----------------------------------------------------------\n\t" +
						"Local: \t\thttp://127.0.0.1:{}\n\t" +
						"External: \thttp://{}:{}\n----------------------------------------------------------",
				env.getProperty("server.port"),
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"));
	}

	/**\
	 * Set a default profile if it has not been set
	 */
	private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
		if (!source.containsProperty("spring.profiles.active")) {
			app.setAdditionalProfiles(SPRING_PROFILE_LOCAL);
		}
	}

	/**
	 * Initializes wiztruck.
	 * <p>
	 * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
	 * <p>
	 */
	@PostConstruct
	public void initApplication() {
		if (env.getActiveProfiles().length == 0) {
			log.warn("No Spring profile configured, running with default configuration");
		} else {
			log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
		}
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application
				.bannerMode(Banner.Mode.OFF)
				.sources(JpaApplication.class);
	}

	/**
	 * Set a default profile if it has not been set.
	 * <p/>
	 * <p>
	 * Please use -Dspring.profiles.active=dev
	 * </p>
	 */
	private String addDefaultProfile() {
		String profile = System.getProperty("spring.profiles.active");
		if (profile != null) {
			log.info("Running with Spring profile(s) : {}", profile);
			return profile;
		}
		log.warn("No Spring profile configured, running with default configuration");
		return SPRING_PROFILE_LOCAL;
	}
}
