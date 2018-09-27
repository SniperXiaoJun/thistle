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

package sviolet.thistle.util.spi;

import sviolet.thistle.util.judge.CheckUtils;

import java.net.URL;
import java.util.*;

import static sviolet.thistle.util.spi.ThistleSpi.*;

/**
 * 服务配置文件加载器
 *
 * @author S.Violet
 */
class ServiceConfigLoader {

    //启动参数指定服务
    private static final String PROPERTY_SERVICE_APPLY_PREFIX = "thistle.spi.apply.";

    //服务配置文件名
    private static final String CONFIG_FILE_SERVICE = "service.properties";
    //服务指定文件名
    private static final String CONFIG_FILE_SERVICE_APPLY = "service-apply.properties";

    private ClassLoader classLoader;
    private SpiLogger logger;
    private int loaderId;

    //service配置信息
    private Map<String, ServiceInfo> serviceInfos = new HashMap<>(8);

    //apply配置信息
    private Map<String, ApplyInfo> applyInfos = new HashMap<>(8);

    ServiceConfigLoader(ClassLoader classLoader, SpiLogger logger, int loaderId) {
        this.classLoader = classLoader;
        this.logger = logger;
        this.loaderId = loaderId;
    }

    /**
     * 设置日志打印器
     */
    void setLogger(SpiLogger logger){
        this.logger = logger;
    }

    /**
     * 清空配置
     */
    void invalidConfig(){
        serviceInfos.clear();
        applyInfos.clear();
    }

    /**
     * 加载服务
     */
    <T> T loadService(Class<T> type) {
        if (type == null) {
            return null;
        }

        //类名
        String classname = type.getName();
        //获取服务实现信息
        ServiceInfo serviceInfo = serviceInfos.get(classname);

        //不存在服务实现
        if (serviceInfo == null || serviceInfo.appliedService == null) {
            if (debug) {
                logger.print(loaderId + LOG_PREFIX_LOADER + "No service definition found, type:" + type.getName());
            }
            return null;
        }

        //实例化服务
        Object service;
        try {
            Class clazz = classLoader.loadClass(serviceInfo.appliedService.implement);
            service = clazz.newInstance();
        } catch (Exception e) {
            logger.print(loaderId + LOG_PREFIX_LOADER + "ERROR: Service " + serviceInfo.type + " (" + serviceInfo.appliedService.implement + ") instantiation error, config:" + serviceInfo.appliedService.resource, e);
            throw new RuntimeException("ThistleSpi: Service " + serviceInfo.type + " (" + serviceInfo.appliedService.implement + ") instantiation error, config:" + serviceInfo.appliedService.resource, e);
        }
        if (!type.isAssignableFrom(service.getClass())) {
            logger.print(loaderId + LOG_PREFIX_LOADER + "ERROR: " + serviceInfo.appliedService.implement + " is not instance of " + serviceInfo.type + ", illegal config:" + serviceInfo.appliedService.resource);
            throw new RuntimeException("ThistleSpi: " + serviceInfo.appliedService.implement + " is not instance of " + serviceInfo.type + ", illegal config:" + serviceInfo.appliedService.resource);
        }
        if (debug) {
            logger.print(loaderId + LOG_PREFIX_LOADER + "Service " + serviceInfo.type + " (" + serviceInfo.appliedService.implement + ") loaded successfully");
        }
        return (T) service;
    }

    void loadConfig(String configPath){

        if (debug) {
            logger.print(loaderId + LOG_PREFIX + "-------------------------------------------------------------");
            logger.print(loaderId + LOG_PREFIX + "Loading services from " + configPath + ", DOC: https://github.com/shepherdviolet/thistle");
        }

        //loading service.properties

        //加载所有service.properties配置文件
        String serviceConfigFile = configPath + CONFIG_FILE_SERVICE;
        Enumeration<URL> urls;
        try {
            urls = classLoader.getResources(serviceConfigFile);
        } catch (Exception e) {
            logger.print(loaderId + LOG_PREFIX + "ERROR: Error while loading " + serviceConfigFile, e);
            throw new RuntimeException("ThistleSpi: Error while loading " + serviceConfigFile, e);
        }

        if (urls == null || !urls.hasMoreElements()) {
            if (debug) {
                logger.print(loaderId + LOG_PREFIX + "No " + serviceConfigFile + " found in classpath");
            }
            return;
        }

        //遍历所有service.properties配置文件
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String urlStr = String.valueOf(url);

            if (debug) {
                logger.print(loaderId + LOG_PREFIX + "Loading " + url);
            }

            //装载配置
            Properties properties;
            try {
                properties = new Properties();
                properties.load(url.openStream());
            } catch (Exception e) {
                logger.print(loaderId + LOG_PREFIX + "ERROR: Error while loading config " + urlStr, e);
                throw new RuntimeException("ThistleSpi: Error while loading config " + urlStr, e);
            }

            if (properties.size() <= 0) {
                if (debug) {
                    logger.print(loaderId + LOG_PREFIX + "Warning: No properties in " + url);
                }
            }

            //遍历所有key-value
            Enumeration<?> names = properties.propertyNames();
            while (names.hasMoreElements()) {
                String key = String.valueOf(names.nextElement());

                //拆解key
                String[] keyItems = key.split(">");
                if (keyItems.length != 3) {
                    logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal key in config file, key:" + key + ", correct format:interface>id>level=impl, config:" + urlStr);
                    throw new RuntimeException("ThistleSpi: Illegal key in config file, key:" + key + ", correct format:interface>id>level=impl, config:" + urlStr);
                }

                String type = keyItems[0];
                String id = keyItems[1];
                Level level = Level.parse(keyItems[2]);

                if (level == Level.UNDEFINED) {
                    logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal config, undefined level " + level + ", should be library/platform/application, in key:" + key + ", config:" + urlStr);
                    throw new RuntimeException("ThistleSpi: Illegal config, undefined level " + level + ", should be library/platform/application, in key:" + key + ", config:" + urlStr);
                }

                //遇到新的服务接口, 则创建一个对象
                ServiceInfo serviceInfo = serviceInfos.get(type);
                if (serviceInfo == null) {
                    serviceInfo = new ServiceInfo();
                    serviceInfo.type = type;
                    serviceInfos.put(type, serviceInfo);
                }

                //实现类
                String implement = properties.getProperty(key);
                if (CheckUtils.isEmptyOrBlank(implement)) {
                    logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal config, value of " + key + " is empty, config:" + urlStr);
                    throw new RuntimeException("ThistleSpi: Illegal config, value of " + key + " is empty, config:" + urlStr);
                }

                //服务接口信息
                Service service = new Service();
                service.id = id;
                service.level = level;
                service.implement = implement;
                service.resource = urlStr;

                Service previous = serviceInfo.definedServices.get(id);
                //若有重复id, 则抛出异常
                if (previous != null) {
                    logger.print(loaderId + LOG_PREFIX + "ERROR: Duplicate service defined with same id, type:" + type + ", id:" + id + ", url1:" + url + ", url2:" + previous.resource);
                    throw new RuntimeException("ThistleSpi: Duplicate service defined with same id, type:" + type + ", id:" + id + ", url1:" + url + ", url2:" + previous.resource);
                }

                serviceInfo.definedServices.put(id, service);

            }

        }

        //loading service-apply.properties

        //加载所有service-apply.properties配置文件
        String applyConfigFile = configPath + CONFIG_FILE_SERVICE_APPLY;
        try {
            urls = classLoader.getResources(applyConfigFile);
        } catch (Exception e) {
            logger.print(loaderId + LOG_PREFIX + "ERROR: Error while loading config " + applyConfigFile, e);
            throw new RuntimeException("ThistleSpi: Error while loading config " + applyConfigFile, e);
        }

        //遍历所有service-apply.properties配置文件
        while (urls != null && urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String urlStr = String.valueOf(url);

            if (debug) {
                logger.print(loaderId + LOG_PREFIX + "loading " + url);
            }

            //装载配置文件
            Properties properties;
            try {
                properties = new Properties();
                properties.load(url.openStream());
            } catch (Exception e) {
                logger.print(loaderId + LOG_PREFIX + "ERROR: Error while loading config " + urlStr, e);
                throw new RuntimeException("ThistleSpi: Error while loading config " + urlStr, e);
            }

            if (properties.size() <= 0) {
                if (debug) {
                    logger.print(loaderId + LOG_PREFIX + "Warning: No properties in " + url);
                }
            }

            //遍历所有key-value
            Enumeration<?> names = properties.propertyNames();
            while (names.hasMoreElements()) {
                String type = String.valueOf(names.nextElement());
                String id = properties.getProperty(type);
                if (CheckUtils.isEmptyOrBlank(id)) {
                    logger.print(loaderId + LOG_PREFIX + "ERROR: Illegal config, value of " + type + " is empty, config:" + urlStr);
                    throw new RuntimeException("ThistleSpi: Illegal config, value of " + type + " is empty, config:" + urlStr);
                }

                if (applyInfos.containsKey(type)) {
                    //apply配置重复处理
                    ApplyInfo previous = applyInfos.get(type);
                    if (id.equals(previous.id)){
                        //若id相同, 不抛出错误, 仅做提醒
                        if (debug) {
                            logger.print(loaderId + LOG_PREFIX + "Warning: Duplicate apply defined with same value, key:" + type + ", value:" + id + ", url1:" + url + ", url2:" + previous.resource);
                        }
                    } else {
                        //若id不相同, 则需要抛出异常
                        String idFromJvmArgs = System.getProperty(PROPERTY_SERVICE_APPLY_PREFIX + type);
                        //允许使用-Dthistle.spi.apply解决apply冲突
                        //we can use -Dthistle.spi.apply to resolve duplicate error
                        String duplicateError = "Duplicate apply defined with different value, key:" + type + ", value1:" + id + ", value2:" + previous.id + ", url1:" + url + ", url2:" + previous.resource;
                        if (CheckUtils.isEmptyOrBlank(idFromJvmArgs)) {
                            //如果没有-Dthistle.spi.apply, 直接抛出异常
                            //no -Dthistle.spi.apply, throw exception
                            logger.print(loaderId + LOG_PREFIX + "ERROR: " + duplicateError);
                            throw new RuntimeException("ThistleSpi: " + duplicateError);
                        } else {
                            //如果有-Dthistle.spi.apply, 先放一马
                            //try with -Dthistle.spi.apply
                            previous.duplicateError = duplicateError;
                            if (debug) {
                                logger.print(loaderId + LOG_PREFIX + "Warning: (Resolve by -Dthistle.spi.apply)" + duplicateError);
                            }
                        }
                    }
                    continue;
                }

                //创建apply信息
                ApplyInfo applyInfo = new ApplyInfo();
                applyInfo.type = type;
                applyInfo.id = properties.getProperty(type);
                applyInfo.resource = urlStr;
                applyInfos.put(type, applyInfo);

            }

        }

        //apply service

        //遍历所有服务
        for (ServiceInfo spi : serviceInfos.values()) {

            //优先用-Dthistle.spi.apply选择服务实现
            String applyId = System.getProperty(PROPERTY_SERVICE_APPLY_PREFIX + spi.type);
            if (!CheckUtils.isEmptyOrBlank(applyId)) {
                Service service = spi.definedServices.get(applyId);
                if (service != null) {
                    spi.appliedService = service;
                    spi.applyReason = "-D" + PROPERTY_SERVICE_APPLY_PREFIX + spi.type + "=" + applyId;
                    continue;
                }
                if (debug) {
                    logger.print(loaderId + LOG_PREFIX + "Warning: No service named " + applyId + ", failed to apply service '" + spi.type + "' to id '" + applyId + "' by -D" + PROPERTY_SERVICE_APPLY_PREFIX + spi.type + "=" + applyId);
                }
            }

            //然后用apply配置选择服务实现
            if (applyInfos.containsKey(spi.type)){
                ApplyInfo applyInfo = applyInfos.get(spi.type);
                if (applyInfo.duplicateError != null) {
                    logger.print(loaderId + LOG_PREFIX + "ERROR: " + applyInfo.duplicateError);
                    throw new RuntimeException("ThistleSpi: " + applyInfo.duplicateError);
                }
                Service service = spi.definedServices.get(applyInfo.id);
                if (service != null) {
                    spi.appliedService = service;
                    spi.applyReason = applyInfo.resource;
                    continue;
                }
                if (debug) {
                    logger.print(loaderId + LOG_PREFIX + "Warning: No service named " + applyInfo.id + ", failed to apply service '" + spi.type + "' to id '" + applyInfo.id + "' by " + applyInfo.resource);
                    logger.print(loaderId + LOG_PREFIX + "Warning: We will apply '" + spi.type + "' service by level automatically (application > platform > library)");
                }
            }

            //最后用优先级选择服务实现

            List<Service> appliedServices = new ArrayList<>(1);
            int highestPriority = -1;
            for (Service service : spi.definedServices.values()) {
                if (service.level.getPriority() > highestPriority) {
                    appliedServices.clear();
                    appliedServices.add(service);
                    highestPriority = service.level.getPriority();
                } else if (service.level.getPriority() == highestPriority) {
                    appliedServices.add(service);
                }
            }

            if (appliedServices.size() <= 0) {
                continue;
            }

            if (appliedServices.size() > 1) {
                StringBuilder stringBuilder = new StringBuilder("Duplicate service defined with same level, type:" + spi.type + ", conflicts:");
                for (Service service : appliedServices) {
                    stringBuilder.append(service);
                    stringBuilder.append("|");
                }
                logger.print(loaderId + LOG_PREFIX + "ERROR: " + stringBuilder.toString());
                throw new RuntimeException("ThistleSpi: " + stringBuilder.toString());
            }

            spi.appliedService = appliedServices.get(0);
            spi.applyReason = "level (application > platform > library)";

        }

        if (debug) {

            for (ServiceInfo serviceInfo : serviceInfos.values()) {

                logger.print(loaderId + LOG_PREFIX + "-------------------------------------------------------------");
                logger.print(loaderId + LOG_PREFIX + "Service Applied:");
                logger.print(loaderId + LOG_PREFIX + "  type: " + serviceInfo.type);
                logger.print(loaderId + LOG_PREFIX + "  implement: " + serviceInfo.appliedService.implement);
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

    private static class ServiceInfo {

        private String type;
        private Service appliedService;
        private String applyReason;
        private Map<String, Service> definedServices = new HashMap<>(1);

    }

    private static class Service {

        private String id;
        private Level level;
        private String implement;
        private String resource;

        @Override
        public String toString() {
            return "Service{" +
                    "id=" + id +
                    ", level=" + level +
                    ", impl=" + implement +
                    ", url=" + resource +
                    '}';
        }
    }

    private static class ApplyInfo {

        private String type;
        private String id;
        private String resource;
        private String duplicateError;

    }

    private enum Level {

        LIBRARY(0),
        PLATFORM(1),
        APPLICATION(2),
        UNDEFINED(-1);

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