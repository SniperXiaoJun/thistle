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
 * 服务加载工厂
 *
 * @author S.Violet
 */
class ServiceFactory {

    //basic
    private ClassLoader classLoader;
    private SpiLogger logger;
    private int loaderId;

    //service配置信息
    private Map<String, ServiceInfo> serviceInfos = new HashMap<>(8);

    //apply配置信息
    private Map<String, ApplyInfo> applyInfos = new HashMap<>(8);

    ServiceFactory(ClassLoader classLoader, SpiLogger logger, int loaderId) {
        this.classLoader = classLoader;
        this.logger = logger;
        this.loaderId = loaderId;
    }

    void setLogger(SpiLogger logger){
        this.logger = logger;
    }

    void invalidConfig(){
        serviceInfos.clear();
        applyInfos.clear();
    }

    /**
     * 加载实例
     */
    <T> T loadInstance(Class<T> type) {
        if (type == null) {
            return null;
        }

        //类名
        String classname = type.getName();
        //获取服务实现信息
        ServiceInfo serviceInfo = serviceInfos.get(classname);

        //不存在服务实现
        if (serviceInfo == null || serviceInfo.appliedService == null) {
            if (LOG_LV >= INFO) {
                logger.print(loaderId + LOG_PREFIX_LOADER + "No service definition found, type:" + type.getName());
            }
            return null;
        }

        //实例化服务
        Object service;
        try {
            Class clazz = classLoader.loadClass(serviceInfo.appliedService.implement);
            service = InstantiationUtils.newInstance(clazz, serviceInfo.appliedService.arg, classLoader,
                    serviceInfo.appliedService.configPath, serviceInfo.appliedService.resource, logger, loaderId);
        } catch (Exception e) {
            logger.print(loaderId + LOG_PREFIX_LOADER + "ERROR: Service " + serviceInfo.type + " (" + serviceInfo.appliedService.implement + ") instantiation error, config:" + serviceInfo.appliedService.resource, e);
            throw new RuntimeException("ThistleSpi: Service " + serviceInfo.type + " (" + serviceInfo.appliedService.implement + ") instantiation error, config:" + serviceInfo.appliedService.resource, e);
        }

        //check instance type
        if (!type.isAssignableFrom(service.getClass())) {
            RuntimeException e = new RuntimeException("ThistleSpi: " + serviceInfo.appliedService.implement + " is not instance of " + serviceInfo.type + ", illegal config:" + serviceInfo.appliedService.resource);
            logger.print(loaderId + LOG_PREFIX_LOADER + "ERROR: " + serviceInfo.appliedService.implement + " is not instance of " + serviceInfo.type + ", illegal config:" + serviceInfo.appliedService.resource, e);
            throw e;
        }

        //log
        if (LOG_LV >= INFO) {
            logger.print(loaderId + LOG_PREFIX_LOADER + "Service loaded successfully: " + serviceInfo.type + " (" + serviceInfo.appliedService.implement + ")");
        }
        return (T) service;
    }

    /**
     * 加载配置文件
     */
    void loadConfig(String configPath, boolean loadingLogger){

        if ((loadingLogger && LOG_LV >= DEBUG) ||
                (!loadingLogger && LOG_LV >= INFO)) {
            logger.print(loaderId + LOG_PREFIX + "-------------------------------------------------------------");
            logger.print(loaderId + LOG_PREFIX + "Loading services from " + configPath + ", DOC: github.com/shepherdviolet/thistle/blob/master/docs/thistlespi/guide.md");
        }

        //loading service.properties

        //加载所有service.properties配置文件
        Enumeration<URL> urls = ParseUtils.loadAllUrls(configPath + CONFIG_FILE_SERVICE, classLoader, true, logger, loaderId);
        if (urls == null) {
            return;
        }

        //处理所有service.properties配置文件
        while (urls.hasMoreElements()) {
            loadServiceProperties(urls.nextElement(), configPath);
        }

        //loading service-apply.properties

        //加载所有service-apply.properties配置文件
        urls = ParseUtils.loadAllUrls(configPath + CONFIG_FILE_SERVICE_APPLY, classLoader, false, logger, loaderId);

        //遍历所有service-apply.properties配置文件
        while (urls != null && urls.hasMoreElements()) {
            loadServiceApplyProperties(urls.nextElement());
        }

        //apply service

        //遍历所有服务
        for (ServiceInfo serviceInfo : serviceInfos.values()) {
            handleServices(serviceInfo);
        }

        if ((loadingLogger && LOG_LV >= DEBUG) ||
                (!loadingLogger && LOG_LV >= INFO)) {

            for (ServiceInfo serviceInfo : serviceInfos.values()) {

                logger.print(loaderId + LOG_PREFIX + "-------------------------------------------------------------");
                logger.print(loaderId + LOG_PREFIX + "Service Applied:");
                logger.print(loaderId + LOG_PREFIX + "  type: " + serviceInfo.type);
                logger.print(loaderId + LOG_PREFIX + "  implement: " + serviceInfo.appliedService.implement +
                        (serviceInfo.appliedService.arg != null ? "(" + serviceInfo.appliedService.arg + ")" : ""));

                if (LOG_LV >= DEBUG) {
                    logger.print(loaderId + LOG_PREFIX + "  url: " + serviceInfo.appliedService.resource);
                    logger.print(loaderId + LOG_PREFIX + "  reason: Applied by " + serviceInfo.applyReason);
                    logger.print(loaderId + LOG_PREFIX + "All Configurations:");
                    for (Service service : serviceInfo.definedServices.values()) {
                        if (service == serviceInfo.appliedService) {
                            logger.print(loaderId + LOG_PREFIX + "  + " + service);
                        } else {
                            logger.print(loaderId + LOG_PREFIX + "  - " + service);
                        }
                    }
                }

            }

        }

    }

    /**
     * 解析服务定义文件为服务信息
     */
    private void loadServiceProperties(URL url, String configPath) {
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
            if (keyItems.length != 3) {
                RuntimeException e = new RuntimeException("ThistleSpi: Illegal key in config file, key:" + key + ", correct format:interface>id>level=impl, definitions:" + url);
                logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal key in config file, key:" + key + ", correct format:interface>id>level=impl, definitions:" + url, e);
                throw e;
            }

            String type = keyItems[0].trim();
            String id = keyItems[1].trim();
            Level level = Level.parse(keyItems[2].trim());

            if (level == Level.UNDEFINED) {
                RuntimeException e = new RuntimeException("ThistleSpi: Illegal config, undefined level " + level + ", should be library/platform/application, in key:" + key + ", definitions:" + url);
                logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal config, undefined level " + level + ", should be library/platform/application, in key:" + key + ", definitions:" + url, e);
                throw e;
            }

            //遇到新的服务接口, 则创建一个对象
            ServiceInfo serviceInfo = serviceInfos.get(type);
            if (serviceInfo == null) {
                serviceInfo = new ServiceInfo();
                serviceInfo.type = type;
                serviceInfos.put(type, serviceInfo);
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
            Service service = new Service();
            service.id = id;
            service.level = level;
            service.implement = implementation.implement;
            service.arg = implementation.arg;
            service.configPath = configPath;
            service.resource = url;

            Service previous = serviceInfo.definedServices.get(id);
            //若有重复id, 则抛出异常
            if (previous != null) {
                RuntimeException e = new RuntimeException("ThistleSpi: Duplicate service defined with same id, type:" + type + ", id:" + id + ", url1:" + url + ", url2:" + previous.resource);
                logger.print(loaderId + LOG_PREFIX + "ERROR: Duplicate service defined with same id, type:" + type + ", id:" + id + ", url1:" + url + ", url2:" + previous.resource, e);
                throw e;
            }

            serviceInfo.definedServices.put(id, service);

        }
    }

    /**
     * 解析服务应用定义文件为应用信息
     */
    private void loadServiceApplyProperties(URL url) {
        //装载配置文件
        List<KeyValue<String, String>> properties = ParseUtils.loadProperties(url, logger, loaderId);
        if (properties == null) {
            return;
        }

        if (properties.size() <= 0) {
            if (LOG_LV >= INFO) {
                logger.print(loaderId + LOG_PREFIX + "Warning: No properties in config " + url);
            }
            return;
        }

        //遍历所有key-value
        for (KeyValue<String, String> keyValue : properties) {
            String type = String.valueOf(keyValue.key()).trim();
            String id = keyValue.value();
            if (CheckUtils.isEmptyOrBlank(id)) {
                RuntimeException e = new RuntimeException("ThistleSpi: Illegal config, value of " + type + " is empty, definitions:" + url);
                logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal config, value of " + type + " is empty, definitions:" + url, e);
                throw e;
            }
            id = id.trim();

            if (applyInfos.containsKey(type)) {
                //apply配置重复处理
                ApplyInfo previous = applyInfos.get(type);
                if (id.equals(previous.id)){
                    //若id相同, 不抛出错误, 仅做提醒
                    if (LOG_LV >= INFO) {
                        logger.print(loaderId + LOG_PREFIX + "Warning: Duplicate apply defined with same value, key:" + type + ", value:" + id + ", url1:" + url + ", url2:" + previous.resource);
                    }
                } else {
                    //若id不相同, 则需要抛出异常
                    String idFromJvmArgs = System.getProperty(STARTUP_PROP_SERVICE_APPLY_PREFIX + type);
                    //允许使用-Dthistle.spi.apply解决apply冲突
                    //we can use -Dthistle.spi.apply to resolve duplicate error
                    String duplicateError = "Duplicate apply defined with different value, key:" + type + ", value1:" + id + ", value2:" + previous.id + ", url1:" + url + ", url2:" + previous.resource;
                    if (CheckUtils.isEmptyOrBlank(idFromJvmArgs)) {
                        //如果没有-Dthistle.spi.apply, 直接抛出异常
                        //no -Dthistle.spi.apply, throw exception
                        RuntimeException e = new RuntimeException("ThistleSpi: " + duplicateError);
                        logger.print(loaderId + LOG_PREFIX + "ERROR: " + duplicateError, e);
                        throw e;
                    } else {
                        //如果有-Dthistle.spi.apply, 先放一马
                        //try with -Dthistle.spi.apply
                        previous.duplicateError = duplicateError;
                        if (LOG_LV >= INFO) {
                            logger.print(loaderId + LOG_PREFIX + "Warning: (Resolve by -Dthistle.spi.apply)" + duplicateError);
                        }
                    }
                }
                continue;
            }

            //创建apply信息
            ApplyInfo applyInfo = new ApplyInfo();
            applyInfo.type = type;
            applyInfo.id = id;
            applyInfo.resource = url;
            applyInfos.put(type, applyInfo);

        }
    }

    /**
     * 决定每个服务接口最终应用哪个服务
     */
    private void handleServices(ServiceInfo serviceInfo) {
        //优先用-Dthistle.spi.apply选择服务实现
        String applyId = System.getProperty(STARTUP_PROP_SERVICE_APPLY_PREFIX + serviceInfo.type);
        if (!CheckUtils.isEmptyOrBlank(applyId)) {
            Service service = serviceInfo.definedServices.get(applyId);
            if (service != null) {
                serviceInfo.appliedService = service;
                serviceInfo.applyReason = "-D" + STARTUP_PROP_SERVICE_APPLY_PREFIX + serviceInfo.type + "=" + applyId;
                return;
            }
            if (LOG_LV >= INFO) {
                logger.print(loaderId + LOG_PREFIX + "Warning: No service named " + applyId + ", failed to apply service '" + serviceInfo.type + "' to id '" + applyId + "' by -D" + STARTUP_PROP_SERVICE_APPLY_PREFIX + serviceInfo.type + "=" + applyId);
            }
        }

        //然后用apply配置选择服务实现
        if (applyInfos.containsKey(serviceInfo.type)){
            ApplyInfo applyInfo = applyInfos.get(serviceInfo.type);
            if (applyInfo.duplicateError != null) {
                RuntimeException e = new RuntimeException("ThistleSpi: " + applyInfo.duplicateError);
                logger.print(loaderId + LOG_PREFIX + "ERROR: " + applyInfo.duplicateError, e);
                throw e;
            }
            Service service = serviceInfo.definedServices.get(applyInfo.id);
            if (service != null) {
                serviceInfo.appliedService = service;
                serviceInfo.applyReason = String.valueOf(applyInfo.resource);
                return;
            }
            if (LOG_LV >= INFO) {
                logger.print(loaderId + LOG_PREFIX + "Warning: No service named " + applyInfo.id + ", failed to apply service '" + serviceInfo.type + "' to id '" + applyInfo.id + "' by " + applyInfo.resource);
                logger.print(loaderId + LOG_PREFIX + "Warning: We will apply '" + serviceInfo.type + "' service by level (application > platform > library > default)");
            }
        }

        //最后用优先级选择服务实现

        List<Service> appliedServices = new ArrayList<>(1);
        int highestPriority = -1;
        for (Service service : serviceInfo.definedServices.values()) {
            if (service.level.getPriority() > highestPriority) {
                appliedServices.clear();
                appliedServices.add(service);
                highestPriority = service.level.getPriority();
            } else if (service.level.getPriority() == highestPriority) {
                appliedServices.add(service);
            }
        }

        if (appliedServices.size() <= 0) {
            return;
        }

        if (appliedServices.size() > 1) {
            StringBuilder stringBuilder = new StringBuilder("Duplicate service defined with same level, type:" + serviceInfo.type + ", conflicts:");
            for (Service service : appliedServices) {
                stringBuilder.append(service);
                stringBuilder.append("|");
            }
            RuntimeException e = new RuntimeException("ThistleSpi: " + stringBuilder.toString());
            logger.print(loaderId + LOG_PREFIX + "ERROR: " + stringBuilder.toString(), e);
            throw e;
        }

        serviceInfo.appliedService = appliedServices.get(0);
        serviceInfo.applyReason = "level (application > platform > library > default)";
    }

    /* ***************************************************************************************************************** */

    /**
     * 一个接口的所有服务信息
     */
    private static class ServiceInfo {

        //接口类
        private String type;
        //应用的服务
        private Service appliedService;
        //应用的原因
        private String applyReason;
        //所有服务定义
        private Map<String, Service> definedServices = new HashMap<>(1);

    }

    /**
     * 服务信息
     */
    private static class Service {

        //id
        private String id;
        //级别
        private Level level;
        //实现类
        private String implement;
        //构造参数
        private String arg;
        //配置文件所在路径
        private String configPath;
        //配置文件URL
        private URL resource;

        @Override
        public String toString() {
            return "Service{" +
                    "id=" + id +
                    ", level=" + level +
                    ", impl=" + implement +
                    ", arg=" + arg +
                    ", url=" + resource +
                    '}';
        }
    }

    /**
     * 应用信息
     */
    private static class ApplyInfo {

        //接口类
        private String type;
        //指定的id
        private String id;
        //配置文件URL
        private URL resource;
        //重复错误
        private String duplicateError;

    }

    /**
     * 扩展级别
     */
    private enum Level {

        /**
         * 默认级
         */
        DEFAULT(0),

        /**
         * 开源扩展库 开源插件库 级别
         */
        LIBRARY(8),

        /**
         * 用户项目的平台框架 级别
         */
        PLATFORM(16),

        /**
         * 用户项目的应用 级别
         */
        APPLICATION(32),

        /**
         * 未定义的级别
         */
        UNDEFINED(Integer.MIN_VALUE);

        //The higher the value, the higher the priority
        private int priority;

        Level(int priority) {
            this.priority = priority;
        }

        private int getPriority(){
            return priority;
        }

        private static Level parse(String level){
            if (level == null) {
                return null;
            }
            switch (level.toUpperCase()) {
                case "DEFAULT":
                    return DEFAULT;
                case "LIBRARY":
                    return LIBRARY;
                case "PLATFORM":
                    return PLATFORM;
                case "APPLICATION":
                    return APPLICATION;
                default:
                    return UNDEFINED;
            }
        }

    }

}
