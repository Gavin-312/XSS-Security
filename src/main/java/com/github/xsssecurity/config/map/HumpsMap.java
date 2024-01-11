package com.github.xsssecurity.config.map;

import java.util.HashMap;
import java.util.Map;

/**
 * 驼峰映射
 *
 * @author Tao.Gan
 * @date 2023/12/01
 */
public class HumpsMap<K, V> extends HashMap<K, V> {
    private final String split;

    public HumpsMap(Map<K, V> m) {
        this(m, "-");
    }

    public HumpsMap(Map<K, V> m, String split) {
        super(m);
        this.split = split;
    }

    @Override
    public V get(Object key) {
        V v = super.get(key);
        if (v == null && key != null) {
            v = super.get(convertKey(key.toString()));
        }
        return v;
    }

    private String convertKey(String key) {
        if (key.contains(split)) {
            return convertToCamelCase(key);
        } else {
            return convertToHyphenCase(key);
        }
    }

    private String convertToHyphenCase(String key) {
        StringBuilder hyphenCase = new StringBuilder();
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (Character.isUpperCase(c)) {
                hyphenCase.append(split);
                hyphenCase.append(Character.toLowerCase(c));
            } else {
                hyphenCase.append(c);
            }
        }
        return hyphenCase.toString();
    }

    private String convertToCamelCase(String input) {
        String[] words = input.split(split);
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                sb.append(word);
            } else {
                sb.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1));
            }
        }
        return sb.toString();
    }

}

