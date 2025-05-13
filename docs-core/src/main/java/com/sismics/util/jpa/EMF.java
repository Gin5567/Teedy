package com.sismics.util.jpa;

import com.google.common.base.Strings;
import com.sismics.docs.core.util.DirectoryUtil;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Entity manager factory.
 * 
 * @author jtremeaux
 */
public final class EMF {
    private static final Logger log = LoggerFactory.getLogger(EMF.class);

    private static Properties properties;

    private static EntityManagerFactory emfInstance;

    static {
        try {
            properties = getEntityManagerProperties();

            log.info("JDBC URL = {}", properties.getProperty("javax.persistence.jdbc.url"));
            log.info("JDBC Driver = {}", properties.getProperty("javax.persistence.jdbc.driver"));

            ConfigurationHelper.resolvePlaceHolders(properties);
            ServiceRegistry reg = new StandardServiceRegistryBuilder().applySettings(properties).build();

            DbOpenHelper openHelper = new DbOpenHelper(reg) {
                @Override
                public void onCreate() throws Exception {
                    executeAllScript(0);
                }

                @Override
                public void onUpgrade(int oldVersion, int newVersion) throws Exception {
                    for (int version = oldVersion + 1; version <= newVersion; version++) {
                        executeAllScript(version);
                    }
                }
            };
            openHelper.open();
            
            emfInstance = Persistence.createEntityManagerFactory("transactions-optional", getEntityManagerProperties());
            
        } catch (Throwable t) {
            log.error("Error creating EMF", t);
        }
    }

    private static Properties getEntityManagerProperties() {
        try {
            URL hibernatePropertiesUrl = EMF.class.getResource("/hibernate.properties");
            if (hibernatePropertiesUrl != null) {
                log.info("✅ Configuring EntityManager from hibernate.properties");

                try (InputStream is = hibernatePropertiesUrl.openStream()) {
                    Properties properties = new Properties();
                    properties.load(is);
                    return properties;
                }
            } else {
                log.warn("⚠️ hibernate.properties not found. Falling back to empty properties.");
            }
        } catch (IOException | IllegalArgumentException e) {
            log.error("❌ Error reading hibernate.properties", e);
        }
        return new Properties(); // fallback to avoid null
    }


    /**
     * Private constructor.
     */
    private EMF() {
    }

    /**
     * Returns an instance of EMF.
     * 
     * @return Instance of EMF
     */
    public static EntityManagerFactory get() {
        return emfInstance;
    }

    public static boolean isDriverH2() {
        String driver = getDriver();
        return driver.contains("h2");
    }

    public static boolean isDriverPostgresql() {
        String driver = getDriver();
        return driver.contains("postgresql");
    }

    public static String getDriver() {
        return (String) properties.get("hibernate.connection.driver_class");
    }
}
