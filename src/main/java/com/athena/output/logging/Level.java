package com.athena.output.logging;

import java.util.HashMap;
import java.util.Map;

public enum Level {
    CRITICAL(1, "Critical"),
    ERROR(2, "Error"),
    WARNING(3, "Warning"),
    INFO(4, "Info"),
    SUCCESS(5, "Success");

    private final int code;
    private final String levelName;

    private static Map<Integer, Level> codeToLevelMapping;

    Level(int code, String levelName) {
        this.code = code;
        this.levelName = levelName;
    }

    public static Level getLevel(int i) {
        if (codeToLevelMapping == null) {
            initMapping();
        }
        return codeToLevelMapping.get(i);
    }

    private static void initMapping() {
        codeToLevelMapping = new HashMap<>();
        for (Level s : values()) {
            codeToLevelMapping.put(s.code, s);
        }
    }

    public String getLevelName() {
        return levelName;
    }
}
