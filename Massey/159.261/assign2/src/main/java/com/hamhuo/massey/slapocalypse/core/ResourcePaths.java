package com.hamhuo.massey.slapocalypse.core;

public class ResourcePaths {
    public static final String BASE_PATH = "src/main/resources/";
    public static final String UI_PATH = BASE_PATH + "ui/";
    public static final String SOUND_PATH = BASE_PATH + "sound/";
    public static final String MAP_PATH = BASE_PATH + "map.txt";

    public static String getImagePath(String subPath, String filename) {
        return UI_PATH + subPath + "/" + filename;
    }

    public static String getSoundPath(String filename) {
        return SOUND_PATH + filename;
    }
}