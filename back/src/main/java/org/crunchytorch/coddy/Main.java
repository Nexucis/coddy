package org.crunchytorch.coddy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import java.util.Properties;

@SpringBootApplication
public class Main extends SpringBootServletInitializer {

    private static final Logger LOCAL_LOGGER = LoggerFactory.getLogger(Main.class);

    public static final String LINUX_ENV_CONFIG_PATH = "CODDY_CONF_DIRECTORY";

    public static void main(String[] args) {
        new Main().configure(new SpringApplicationBuilder(Main.class)).run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Main.class).properties(getProperties());
    }

    /**
     * @return {@link Properties} which contains some spring variable environment
     */
    private static Properties getProperties() {
        Properties props = new Properties();
        String configEnv = System.getenv(Main.LINUX_ENV_CONFIG_PATH);

        if (!StringUtils.isEmpty(configEnv)) {
            LOCAL_LOGGER.info("Linux variable {} found with a value", Main.LINUX_ENV_CONFIG_PATH);
            // add conf file path
            props.put("spring.config.location", "file:${" + Main.LINUX_ENV_CONFIG_PATH + "}/");

        } else {
            LOCAL_LOGGER.error("Linux variable environment {} is missing. Unable to load external configuration files", Main.LINUX_ENV_CONFIG_PATH);
        }
        return props;
    }
}
