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
import sviolet.thistle.util.judge.CheckUtils;

import java.net.URL;
import java.util.*;

import static sviolet.thistle.x.common.thistlespi.Constants.*;

/**
 * 插件加载工厂
 *
 * @author S.Violet
 */
class PluginFactory {

    //basic
    private ClassLoader classLoader;
    private SpiLogger logger;
    private int loaderId;

    //service配置信息
    private Map<String, PluginInfo> pluginInfos = new HashMap<>(8);

    //apply配置信息
    private Map<String, IgnoreInfo> ignoreInfos = new HashMap<>(8);

    PluginFactory(ClassLoader classLoader, SpiLogger logger, int loaderId) {
        this.classLoader = classLoader;
        this.logger = logger;
        this.loaderId = loaderId;
    }

    void setLogger(SpiLogger logger){
        this.logger = logger;
    }

    void invalidConfig(){
        pluginInfos.clear();
        ignoreInfos.clear();
    }

    /**
     * 加载实例
     */
    <T> List<T> loadInstance(Class<T> type) {
        if (type == null) {
            return null;
        }

        //类名
        String classname = type.getName();
        //获取插件实现信息
        PluginInfo pluginInfo = pluginInfos.get(classname);

        //不存在插件实现
        if (pluginInfo == null || pluginInfo.orderedPlugins == null) {
            if (LOG_LV >= INFO) {
                logger.print(loaderId + LOG_PREFIX_LOADER + "No enabled plugins found, type:" + type.getName());
            }
            return null;
        }

        //plugins instance list
        List<T> plugins = new ArrayList<>(pluginInfo.orderedPlugins.size());

        for (Plugin plugin : pluginInfo.orderedPlugins) {

            //实例化服务
            Object pluginObj;
            try {
                Class clazz = classLoader.loadClass(plugin.implement);
                pluginObj = InstantiationUtils.newInstance(clazz, plugin.arg, classLoader, plugin.configPath, plugin.resource, logger, loaderId);
            } catch (Exception e) {
                logger.print(loaderId + LOG_PREFIX_LOADER + "ERROR: Plugin " + pluginInfo.type + " (" + plugin.implement + ") instantiation error, definitions:" + plugin.resource, e);
                throw new RuntimeException("ThistleSpi: Plugin " + pluginInfo.type + " (" + plugin.implement + ") instantiation error, definitions:" + plugin.resource, e);
            }

            //check instance type
            if (!type.isAssignableFrom(pluginObj.getClass())) {
                RuntimeException e = new RuntimeException("ThistleSpi: " + plugin.implement + " is not instance of " + pluginInfo.type + ", illegal definitions:" + plugin.resource);
                logger.print(loaderId + LOG_PREFIX_LOADER + "ERROR: " + plugin.implement + " is not instance of " + pluginInfo.type + ", illegal definitions:" + plugin.resource, e);
                throw e;
            }

            //add to list
            plugins.add((T) pluginObj);

        }

        //log
        if (LOG_LV >= INFO) {
            StringBuilder stringBuilder = new StringBuilder(loaderId + LOG_PREFIX_LOADER + "Plugin loaded successfully: ");
            stringBuilder.append(pluginInfo.type);
            stringBuilder.append(" (");
            int i = 0;
            for (Plugin plugin : pluginInfo.orderedPlugins) {
                if (LOG_LV < DEBUG && i++ >= MAX_INFO_LOG_LINES) {
                    stringBuilder.append(" ... ");
                    stringBuilder.append(pluginInfo.orderedPlugins.size() - MAX_INFO_LOG_LINES);
                    stringBuilder.append(" more");
                    break;
                }
                stringBuilder.append(" ");
                stringBuilder.append(plugin.implement);
            }
            stringBuilder.append(" )");
            logger.print(stringBuilder.toString());
        }

        return plugins;
    }

    /**
     * 加载配置文件
     */
    void loadConfig(String configPath){

        if (LOG_LV >= INFO) {
            logger.print(loaderId + LOG_PREFIX + "-------------------------------------------------------------");
            logger.print(loaderId + LOG_PREFIX + "Loading plugins from " + configPath + ", DOC: github.com/shepherdviolet/thistle/blob/master/docs/thistlespi/guide.md");
        }

        //loading plugin.properties

        //加载所有plugin.properties配置文件
        Enumeration<URL> urls = ParseUtils.loadAllUrls(configPath + CONFIG_FILE_PLUGIN, classLoader, true, logger, loaderId);
        if (urls == null) {
            return;
        }

        //处理所有plugin.properties配置文件
        while (urls.hasMoreElements()) {
            loadPluginProperties(urls.nextElement(), configPath);
        }

        //loading plugin-ignore.properties

        //加载所有plugin-ignore.properties配置文件
        urls = ParseUtils.loadAllUrls(configPath + CONFIG_FILE_PLUGIN_IGNORE, classLoader, false, logger, loaderId);

        //遍历所有plugin-ignore.properties配置文件
        while (urls != null && urls.hasMoreElements()) {
            loadPluginIgnoreProperties(urls.nextElement());
        }

        //ignore plugins

        //遍历所有插件
        for (PluginInfo pluginInfo : pluginInfos.values()) {
            handlePlugins(pluginInfo);
        }

        if (LOG_LV >= INFO) {

            for (PluginInfo pluginInfo : pluginInfos.values()) {

                logger.print(loaderId + LOG_PREFIX + "-------------------------------------------------------------");
                logger.print(loaderId + LOG_PREFIX + "Plugin Applied:");
                logger.print(loaderId + LOG_PREFIX + "  type: " + pluginInfo.type);
                logger.print(loaderId + LOG_PREFIX + "  implements:");

                int i = 0;
                for (Plugin plugin : pluginInfo.orderedPlugins) {
                    if (LOG_LV < DEBUG && i++ >= MAX_INFO_LOG_LINES) {
                        logger.print(loaderId + LOG_PREFIX + "    ...... " + (pluginInfo.orderedPlugins.size() - MAX_INFO_LOG_LINES) +
                                " more omitted ('-D" + STARTUP_PROP_LOG_LV + "=debug' to show more)");
                        break;
                    }
                    logger.print(loaderId + LOG_PREFIX + "  + " + plugin.toAbstractString());
                }

                if (LOG_LV >= DEBUG) {
                    logger.print(loaderId + LOG_PREFIX + "All Configurations:");
                    for (Plugin plugin : pluginInfo.plugins) {
                        logger.print(loaderId + LOG_PREFIX + (plugin.enabled ? "  + " : "  - ") + plugin);
                    }
                }

            }

        }

    }

    /**
     * 解析插件定义文件为插件信息
     */
    private void loadPluginProperties(URL url, String configPath) {
        //装载配置文件
        List<KeyValue<String, String>> properties = ParseUtils.loadProperties(url, logger, loaderId);
        if (properties == null) {
            return;
        }

        if (properties.size() <= 0) {
            if (LOG_LV >= INFO) {
                logger.print(loaderId + LOG_PREFIX + "Warning: No properties in config " + url);
            }
        }

        //遍历所有key-value
        for (KeyValue<String, String> keyValue : properties) {
            String key = String.valueOf(keyValue.key()).trim();

            //拆解key
            String[] keyItems = key.split(">");
            if (keyItems.length != 2) {
                RuntimeException e = new RuntimeException("ThistleSpi: Illegal key in config file, key:" + key + ", correct format:interface>priority=impl, definitions:" + url);
                logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal key in config file, key:" + key + ", correct format:interface>priority=impl, definitions:" + url, e);
                throw e;
            }

            String type = keyItems[0].trim();
            int priority;
            try {
                priority = Integer.valueOf(keyItems[1].trim());
            } catch (Exception e) {
                logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal config, invalid priority " + keyItems[1] + ", should be integer, in key:" + key + ", definitions:" + url, e);
                throw new RuntimeException("ThistleSpi: Illegal config, invalid priority " + keyItems[1] + ", should be integer, in key:" + key + ", definitions:" + url, e);
            }

            //遇到新的服务接口, 则创建一个对象
            PluginInfo pluginInfo = pluginInfos.get(type);
            if (pluginInfo == null) {
                pluginInfo = new PluginInfo();
                pluginInfo.type = type;
                pluginInfos.put(type, pluginInfo);
            }

            //参数值
            String propValue = keyValue.value();
            if (CheckUtils.isEmptyOrBlank(propValue)) {
                RuntimeException e = new RuntimeException("ThistleSpi: Illegal config, value of " + key + " is empty, definitions:" + url);
                logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal config, value of " + key + " is empty, definitions:" + url, e);
                throw e;
            }
            propValue = propValue.trim();

            //实现类信息
            ParseUtils.Implementation implementation = ParseUtils.parseImplementation(propValue, true, logger, loaderId, key, url);

            //服务接口信息
            Plugin plugin = new Plugin();
            plugin.priority = priority;
            plugin.implement = implementation.implement;
            plugin.arg = implementation.arg;
            plugin.configPath = configPath;
            plugin.resource = url;
            pluginInfo.plugins.add(plugin);

        }
    }

    /**
     * 解析插件忽略定义文件为忽略信息
     */
    private void loadPluginIgnoreProperties(URL url) {
        //装载配置文件
        List<KeyValue<String, String>> properties = ParseUtils.loadProperties(url, logger, loaderId);
        if (properties == null) {
            return;
        }

        if (properties.size() <= 0) {
            if (LOG_LV >= INFO) {
                logger.print(loaderId + LOG_PREFIX + "Warning: No properties in " + url);
            }
            return;
        }

        //遍历所有key-value
        for (KeyValue<String, String> keyValue : properties) {
            String type = String.valueOf(keyValue.key()).trim();
            String ignoreStr = keyValue.value();
            if (CheckUtils.isEmptyOrBlank(ignoreStr)) {
                RuntimeException e = new RuntimeException("ThistleSpi: Illegal config, value of " + type + " is empty, definitions:" + url);
                logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal config, value of " + type + " is empty, definitions:" + url, e);
                throw e;
            }
            ignoreStr = ignoreStr.trim();

            IgnoreInfo ignoreInfo = ignoreInfos.get(type);
            if (ignoreInfo == null) {
                ignoreInfo = new IgnoreInfo();
                ignoreInfo.type = type;
                ignoreInfos.put(type, ignoreInfo);
            }

            String[] ignoreImpls = ignoreStr.split(",");

            for (String ignoreImpl : ignoreImpls) {
                if (ignoreImpl == null) {
                    continue;
                }
                ignoreImpl = ignoreImpl.trim();
                if (ignoreImpl.length() <= 0) {
                    continue;
                }
                Ignore ignore = new Ignore();
                ignore.ignoreImpl = ignoreImpl;
                ignore.resource = url;
                ignoreInfo.ignores.add(ignore);
            }
        }
    }

    /**
     * 决定最终应用那些插件实现, 并排序
     */
    private void handlePlugins(PluginInfo pluginInfo) {
        //优先用-Dthistle.spi.ignore忽略插件实现
        String ignoreStr = System.getProperty(STARTUP_PROP_PLUGIN_IGNORE_PREFIX + pluginInfo.type);
        if (!CheckUtils.isEmptyOrBlank(ignoreStr)) {
            String[] ignoreImpls = ignoreStr.split(",");
            for (String ignoreImpl : ignoreImpls) {
                if (CheckUtils.isEmptyOrBlank(ignoreImpl)) {
                    continue;
                }
                ignoreImpl = ignoreImpl.trim();
                ParseUtils.Implementation implementation = ParseUtils.parseImplementation(ignoreImpl, false, logger, loaderId, ignoreStr, null);
                int count = 0;
                for (Plugin plugin : pluginInfo.plugins) {
                    //ignore中未指定构造参数时排除所有实现, ignore中指定构造参数则排除相同参数的实现
                    if (implementation.implement.equals(plugin.implement) &&
                            (implementation.arg == null || implementation.arg.equals(plugin.arg))) {
                        count++;
                        plugin.enabled = false;
                        plugin.disableReason = "-D" + STARTUP_PROP_PLUGIN_IGNORE_PREFIX + pluginInfo.type + "=" + ignoreStr;
                    }
                }
                if (LOG_LV >= INFO && count <= 0) {
                    logger.print(loaderId + LOG_PREFIX + "Warning: Plugin implement " + ignoreImpl + " undefined, failed to ignore implement '" + ignoreImpl + "' of '" + pluginInfo.type + "' by -D" + STARTUP_PROP_PLUGIN_IGNORE_PREFIX + pluginInfo.type + "=" + ignoreStr);
                }
            }
        }

        //然后用配置忽略插件实现
        if (ignoreInfos.containsKey(pluginInfo.type)){
            IgnoreInfo ignoreInfo = ignoreInfos.get(pluginInfo.type);
            for (Ignore ignore : ignoreInfo.ignores) {
                ParseUtils.Implementation implementation = ParseUtils.parseImplementation(ignore.ignoreImpl, false, logger, loaderId, pluginInfo.type, ignore.resource);
                int count = 0;
                for (Plugin plugin : pluginInfo.plugins) {
                    if (implementation.implement.equals(plugin.implement) &&
                            (implementation.arg == null || implementation.arg.equals(plugin.arg))) {
                        count++;
                        plugin.enabled = false;
                        plugin.disableReason = String.valueOf(ignore.resource);
                    }
                }
                if (LOG_LV >= INFO && count <= 0) {
                    logger.print(loaderId + LOG_PREFIX + "Warning: Plugin implement " + ignore.ignoreImpl + " undefined, failed to ignore implement '" + ignore.ignoreImpl + "' of '" + pluginInfo.type + "' by " + ignore.resource);
                }
            }
        }

        //最后取可用的插件排序
        for (Plugin plugin : pluginInfo.plugins) {
            if (plugin.enabled) {
                pluginInfo.orderedPlugins.add(plugin);
            }
        }
        Collections.sort(pluginInfo.orderedPlugins, new Comparator<Plugin>() {
            @Override
            public int compare(Plugin o1, Plugin o2) {
                return o1.priority - o2.priority;
            }
        });
    }

    /* ***************************************************************************************************************** */

    /**
     * 一个接口的所有插件信息
     */
    private static class PluginInfo {

        //接口类
        private String type;
        //所有插件定义
        private List<Plugin> plugins = new ArrayList<>(8);
        //所有生效的插件定义(已排序)
        private List<Plugin> orderedPlugins = new ArrayList<>(8);

    }

    /**
     * 插件信息
     */
    private static class Plugin {

        //插件优先级
        private int priority;
        //实现类
        private String implement;
        //构造参数
        private String arg;
        //配置文件所在路径
        private String configPath;
        //配置文件URL
        private URL resource;
        //是否启用
        private boolean enabled = true;
        //禁用原因
        private String disableReason;

        String toAbstractString(){
            return "Plugin{" +
                    "priority=" + priority +
                    ", impl=" + implement +
                    (arg != null ? "(" + arg + ")" : "")+
                    '}';
        }

        @Override
        public String toString() {
            return "Plugin{" +
                    "priority=" + priority +
                    ", impl=" + implement +
                    (arg != null ? "(" + arg + ")" : "")+
                    (enabled ? "" : ", disable by " + disableReason) +
                    ", url=" + resource +
                    '}';
        }

    }

    /**
     * 一个接口的所有忽略信息
     */
    private static class IgnoreInfo {

        //接口类
        private String type;
        //所有忽略信息
        private List<Ignore> ignores = new ArrayList<>(8);

    }

    /**
     * 忽略信息
     */
    private static class Ignore {

        //忽略的实现类
        private String ignoreImpl;
        //配置文件URL
        private URL resource;

        @Override
        public String toString() {
            return "Ignore{" +
                    "ignoreImpl=" + ignoreImpl +
                    ", resource=" + resource +
                    '}';
        }

    }

}
