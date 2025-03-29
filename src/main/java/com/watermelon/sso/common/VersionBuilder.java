package com.watermelon.sso.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 获取版本
 *
 * @author watermelon
 */
public class VersionBuilder<T> {

    private static final String GIT_PROPERTY_FILE_NAME = "git.properties";
    private static final String GIT_PROPERTY_BUILD_TIME = "git.build.time";
    private static final String GIT_PROPERTY_DESCRIBE = "git.commit.id.describe";
    public static String version = null;
    public Object target;

    public VersionBuilder(T target) {
        this.target = target;
    }

    public String readVersion() {
        if (version == null) {
            synchronized (target.getClass()) {
                if (version == null) {
                    Properties properties = new Properties();
                    ClassLoader classLoader = target.getClass().getClassLoader();
                    InputStream inStream = classLoader.getResourceAsStream(GIT_PROPERTY_FILE_NAME);
                    if (inStream == null) {
                        return "Invalid version";
                    }

                    try {
                        properties.load(inStream);
                        version = properties.getProperty(GIT_PROPERTY_DESCRIBE) + "-" +
                                properties.getProperty(GIT_PROPERTY_BUILD_TIME);
                    } catch (IOException e) {
                        return "Invalid version";
                    }
                }
            }
        }
        return version;
    }
}