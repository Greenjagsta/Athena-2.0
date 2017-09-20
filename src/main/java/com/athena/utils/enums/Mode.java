/*
 * Copyright (C) 2017 Jack Green
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.athena.utils.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jack
 */
public enum Mode {
    INCREMENTAL(100, "Incremental", false, false, false),
    DICTIONARY(101, "Dictionary", true, false, false),
    MASK(102, "Mask", false, false, true),
    HYBRID(103, "Hybrid", true, false, true),
    COMBINATOR(104, "Combinator", true, true, false),
    PROBABILISTIC(105, "Probabilistic", false, false, false);

    private int code;
    private final String modeName;
    private final boolean requiresDict;
    private final boolean requiresDict2;
    private final boolean requiresMask;

    /**
     * A mapping between the integer code and its corresponding Status to facilitate lookup by code.
     */
    private static Map<Integer, Mode> codeToModeMapping;

    Mode(int code, String modeName, boolean requiresDict, boolean requiresDict2, boolean requiresMask) {
        this.code = code;
        this.modeName = modeName;
        this.requiresDict = requiresDict;
        this.requiresDict2 = requiresDict2;
        this.requiresMask = requiresMask;
    }

    public static Mode getMode(int i) {
        if (codeToModeMapping == null) {
            initMapping();
        }
        return codeToModeMapping.get(i);
    }

    private static void initMapping() {
        codeToModeMapping = new HashMap<>();
        for (Mode s : values()) {
            codeToModeMapping.put(s.code, s);
        }
    }

    public String getModeName() {
        return modeName;
    }

    public boolean requiresDict() {
        return requiresDict;
    }

    public boolean requiresDict2() {
        return requiresDict2;
    }

    public boolean requiresMask() {
        return requiresMask;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Status");
        sb.append("{code=").append(code);
        sb.append(", label='").append(modeName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}