/*
 * Copyright (C) 2015-2018 S.Violet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project GitHub: https://github.com/shepherdviolet/thistle
 * Email: shepherdviolet@163.com
 */

package sviolet.thistle.model.common;

import java.util.Map;

/**
 * 按顺序从一系列Map中取值, 取到为止
 *
 * @author S.Violet
 */
public class PollGettingMap {

    private Map[] maps;
    private String[] keyPrefixes;
    private ParseExceptionHandler exceptionHandler;

    /**
     * 内置数个Map, 按指定顺序轮询获取参数, 第一个优先级最高
     * @param leadingMap 第一个Map, 优先级最高
     * @param followingMaps 其他的Map
     */
    public PollGettingMap(Map leadingMap, Map... followingMaps) {
        if (leadingMap == null) {
            throw new NullPointerException("leadingMap is null");
        }
        if (followingMaps != null) {
            for (Map map : followingMaps) {
                if (map == null) {
                    throw new NullPointerException("one of followingMaps is null");
                }
            }
            this.maps = new Map[followingMaps.length + 1];
            this.keyPrefixes = new String[followingMaps.length + 1];
            this.maps[0] = leadingMap;
            System.arraycopy(followingMaps, 0, this.maps, 1, followingMaps.length);
        } else {
            this.maps = new Map[]{leadingMap};
            this.keyPrefixes = new String[1];
        }
    }

    public PollGettingMap setExceptionHandler(ParseExceptionHandler exceptionHandler){
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    /**
     * 给指定Map添加key前缀, 从该Map中取值时, 会加上前缀, 然后再去get (仅在key类型为String时有效)
     * @param index map编号
     * @param keyPrefix key前缀
     */
    public PollGettingMap setKeyPrefix(int index, String keyPrefix) {
        if (index < 0 || index >= this.keyPrefixes.length) {
            throw new ArrayIndexOutOfBoundsException("index out of bound when you setKeyPrefix, array length:" + keyPrefix.length() + ", your index:" + index);
        }
        this.keyPrefixes[index] = keyPrefix;
        return this;
    }

    /**
     * 按顺序从一系列Map中取值, 取到为止
     * @param key key
     * @param def 默认值
     * @return value
     */
    public Object get(Object key, Object def) {
        Object value;
        for (int i = 0 ; i < maps.length ; i++) {
            Object k = key;
            if (k instanceof String) {
                String keyPrefix;
                if ((keyPrefix = this.keyPrefixes[i]) != null) {
                    k = keyPrefix + k;
                }
            }
            if ((value = maps[i].get(k)) != null) {
                return value;
            }
        }
        return def;
    }

    /**
     * 按顺序从一系列Map中取String, 取到为止, 若类型不为String会转换成String返回
     * @param key key
     * @param def 默认值
     * @return value
     */
    public String getString(Object key, String def) {
        Object value = get(key, def);
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    /**
     * 按顺序从一系列Map中取int, 取到为止, 若类型不为int会转换成int返回, 转换失败抛出异常
     * @param key key
     * @param def 默认值
     * @return value
     */
    public int getInt(Object key, int def) {
        Object value = get(key, def);
        if (value instanceof Integer) {
            return (int) value;
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            handleException(key, value, int.class.getName(), def, e);
            return def;
        }
    }

    /**
     * 按顺序从一系列Map中取long, 取到为止, 若类型不为long会转换成long返回, 转换失败抛出异常
     * @param key key
     * @param def 默认值
     * @return value
     */
    public long getLong(Object key, long def) {
        Object value = get(key, def);
        if (value instanceof Long) {
            return (long) value;
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (Exception e) {
            handleException(key, value, long.class.getName(), def, e);
            return def;
        }
    }

    /**
     * 按顺序从一系列Map中取float, 取到为止, 若类型不为float会转换成float返回, 转换失败抛出异常
     * @param key key
     * @param def 默认值
     * @return value
     */
    public float getFloat(Object key, float def) {
        Object value = get(key, def);
        if (value instanceof Float) {
            return (float) value;
        }
        try {
            return Float.parseFloat(String.valueOf(value));
        } catch (Exception e) {
            handleException(key, value, float.class.getName(), def, e);
            return def;
        }
    }

    /**
     * 按顺序从一系列Map中取double, 取到为止, 若类型不为double会转换成double返回, 转换失败抛出异常
     * @param key key
     * @param def 默认值
     * @return value
     */
    public double getDouble(Object key, double def) {
        Object value = get(key, def);
        if (value instanceof Double) {
            return (double) value;
        }
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (Exception e) {
            handleException(key, value, double.class.getName(), def, e);
            return def;
        }
    }

    private void handleException(Object key, Object value, String toType, Object def, Exception e) throws ParseException {
        if (exceptionHandler == null) {
            throw new ParseException("Error while parsing " + value + " to " + toType, e);
        }
        exceptionHandler.onParseException(key, value, toType, def, e);
    }

    /**
     * 当value解析为所需类型失败时抛出的异常
     */
    public static class ParseException extends RuntimeException {

        public ParseException(String message) {
            super(message);
        }

        public ParseException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    /**
     * 当value解析为所需类型失败时的自定义处理逻辑, 可以抛出ParseException异常, 可以抛出其他RuntimeException, 可以打印日志后返回默认值,
     * 也可以不处理(返回默认值)
     */
    public interface ParseExceptionHandler {

        /**
         * 当value解析为所需类型失败时的自定义处理逻辑, 可以抛出ParseException异常, 可以抛出其他RuntimeException, 可以打印日志后返回默认值,
         * 也可以不处理(返回默认值). 只要不在onParseException方法中抛出异常, 就会返回默认值.
         * @param key key
         * @param value value(就是将它转换为toType时发生了异常)
         * @param toType 应该返回的类型
         * @param def 如果不抛出异常, 会返回该默认值
         * @param e 异常
         */
        void onParseException(Object key, Object value, String toType, Object def, Exception e) throws ParseException;

    }

}
