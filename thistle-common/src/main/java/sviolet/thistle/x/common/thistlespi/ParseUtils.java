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

package sviolet.thistle.x.common.thistlespi;

import sviolet.thistle.entity.set.KeyValue;
import sviolet.thistle.util.conversion.PropertiesUtils;

import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import static sviolet.thistle.x.common.thistlespi.Constants.DEBUG;
import static sviolet.thistle.x.common.thistlespi.Constants.LOG_LV;
import static sviolet.thistle.x.common.thistlespi.Constants.LOG_PREFIX;

/**
 * 配置解析相关
 *
 * @author S.Violet
 */
class ParseUtils {

    /**
     * 从Classpath中找到所有配置文件的URL
     * 返回空: 找不到配置文件
     * @param configPath 配置文件路径
     * @param classLoader 类加载器
     * @param notFoundLog true: 找不到配置文件时打印日志
     * @param logger (日志相关)日志打印器
     * @param loaderId (日志相关)加载器ID
     */
    static Enumeration<URL> loadAllUrls(String configPath, ClassLoader classLoader, boolean notFoundLog, SpiLogger logger, int loaderId) {
        Enumeration<URL> urls;
        try {
            urls = classLoader.getResources(configPath);
        } catch (Exception e) {
            logger.print(loaderId + LOG_PREFIX + "ERROR: Error while loading classpath " + configPath, e);
            throw new RuntimeException("ThistleSpi: Error while loading classpath " + configPath, e);
        }

        if (urls == null || !urls.hasMoreElements()) {
            if (notFoundLog && LOG_LV >= DEBUG) {
                logger.print(loaderId + LOG_PREFIX + "No config " + configPath + " found in classpath");
            }
            return null;
        }
        return urls;
    }

    /**
     * 加载配置文件
     * 返回空: 文件被强制排除
     * @param url 配置文件url
     * @param logger (日志相关)日志打印器
     * @param loaderId (日志相关)加载器ID
     */
    static List<KeyValue<String, String>> loadProperties(URL url, SpiLogger logger, int loaderId){
        try {
            //检查文件是否被强制排除
            if (ExclusionUtils.checkFileExclusion(url, logger, loaderId)) {
                return null;
            }
            return PropertiesUtils.loadAsList(url.openStream());
        } catch (Exception e) {
            logger.print(loaderId + LOG_PREFIX + "ERROR: Error while loading config " + url, e);
            throw new RuntimeException("ThistleSpi: Error while loading config " + url, e);
        }
    }

    /**
     * 将参数值(即配置文件中的value)解析为实现信息(实现类和构造参数)
     * @param propValue 参数值
     * @param fromConfig (日志相关)true:来自配置文件 false:来自启动参数
     * @param logger (日志相关)日志打印器
     * @param loaderId (日志相关)加载器ID
     * @param propKey (日志相关)参数键, 或启动参数名
     * @param configUrl (日志相关)配置文件URL, 可为空
     */
    static Implementation parseImplementation(String propValue, boolean fromConfig, SpiLogger logger, int loaderId, String propKey, URL configUrl){
        int argStart = propValue.indexOf("(");
        //value第一个字符就是(, 非法
        if (argStart == 0) {
            if (fromConfig) {
                RuntimeException e = new RuntimeException("ThistleSpi: Illegal config, value of " + propKey + " starts with '(', definitions:" + configUrl);
                logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal config, value of " + propKey + " starts with '(', definitions:" + configUrl, e);
                throw e;
            } else {
                RuntimeException e = new RuntimeException("ThistleSpi: Illegal jvm arg, value of -D" + propKey + " starts with '('");
                logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal jvm arg, value of -D" + propKey + " starts with '('", e);
                throw e;
            }
        }
        //存在(字符, 尝试截取构造参数
        if (argStart > 0) {
            //value最后一个字符不是), 非法
            if (')' != propValue.charAt(propValue.length() - 1)) {
                if (fromConfig) {
                    RuntimeException e = new RuntimeException("ThistleSpi: Illegal config, value of " + propKey + " has '(' but no ')' at last, definitions:" + configUrl);
                    logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal config, value of " + propKey + " has '(' but no ')' at last, definitions:" + configUrl, e);
                    throw e;
                } else {
                    RuntimeException e = new RuntimeException("ThistleSpi: Illegal jvm arg, value of -D" + propKey + " has '(' but no ')' at last");
                    logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal jvm arg, value of -D" + propKey + " has '(' but no ')' at last", e);
                    throw e;
                }
            }
            return new Implementation(
                    propValue.substring(0, argStart).trim(),
                    propValue.substring(argStart + 1, propValue.length() - 1)
            );
        }
        return new Implementation(propValue, null);
    }

    static class Implementation {
        //class name
        String implement;
        //constructor arg ( string arg or properties file name )
        String arg;
        Implementation(String implement, String arg) {
            this.implement = implement;
            this.arg = arg;
        }
    }

}
