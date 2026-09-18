package com.fooze.sortedcreativetabs;

public abstract class Config {
    private static Config instance;

    public static Config get() {
        if (instance == null) {
            throw new IllegalStateException("Config has not been initialised");
        }

        return instance;
    }

    public static void setInstance(Config config) {
        instance = config;
    }

    // Config values
    public abstract boolean enableSorting();
}