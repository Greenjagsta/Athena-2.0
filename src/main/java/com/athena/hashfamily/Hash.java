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

package com.athena.hashfamily;

import com.athena.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public enum Hash {
    MD5(100, "MD5", com.athena.hashfamily.md.MD5.class, "[a-fA-F0-9]{32}$"),
    SHA1(200, "SHA1", com.athena.hashfamily.sha.SHA1.class, "[a-fA-F0-9]{40}$");
    
    private final int code;
    private final String name;
    private final Class classname;
    private final String regex;
    
    private static Map<Integer, Hash> codeToHashMapping;

    Hash(int code, String name, Class classname, String regex) {
        this.code = code;
        this.name = name;
        this.classname = classname;
        this.regex = regex;
    }
    
    public static Hash getHash(int i) {
        if (codeToHashMapping == null) {
            initMapping();
        }
        return codeToHashMapping.get(i);
    }
    
    private static void initMapping() {
        codeToHashMapping = new HashMap<>();
        for (Hash h : values()) {
            codeToHashMapping.put(h.code, h);
        }
    }
    
    public static ArrayList<Integer> getHashType(String hash) {
        ArrayList<Integer> codes = new ArrayList<>();
        for (Hash h : values()) {
            if ((Pattern.compile(h.getRegex())).matcher(hash).matches()) {
                codes.add(h.getCode());
            }
        }
        return codes;
    }

    public static boolean hashTypeExists(int hashType) {
        for (Hash h : values()) {
            if (h.getCode() == hashType) {
                return true;
            }
        }
        return false;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public Class getClassname() {
        return classname;
    }
    
    public String getRegex() {
        return regex;
    }

    public Method getDigestInstance() {
        try {
            return classname.newInstance().getClass().getMethod("digest", byte[].class);
        } catch (InstantiationException ex) {
            Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}