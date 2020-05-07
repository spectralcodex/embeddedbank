package com.embed.util;

import io.vertx.ext.web.RoutingContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by sfr3man on 30/10/2016.
 */
public class PropertiesCache {

    private final Properties configProp = new Properties();

    private PropertiesCache() {
        //Private constructor to restrict new instances

        //System.out.println("Read all properties from file");
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.properties");
            //FileInputStream in = new java.io.FileInputStream(new File("ws.properties"));
            configProp.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class LazyHolder {
        private static final PropertiesCache INSTANCE = new PropertiesCache();
    }

    public static PropertiesCache getInstance() {
        return LazyHolder.INSTANCE;
    }

    public String getProperty(String key) {
        return configProp.getProperty(key);
    }

   /* public Set<String> getAllPropertyNames() {
        return configProp.stringPropertyNames();
    }

    public boolean containsKey(String key) {
        return configProp.containsKey(key);
    }

    public Map<String, String> getProperties() {
        Stream<Map.Entry<Object, Object>> stream = configProp.entrySet().stream();
        return stream.collect(Collectors.toMap(e -> String.valueOf(e.getKey()), e -> String.valueOf(e.getValue())));
    }*/


}
