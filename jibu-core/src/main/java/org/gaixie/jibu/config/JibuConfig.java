/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.gaixie.jibu.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jibu 配置，加载 jibu.properties 配置文件。
 * <p>
 */
public class JibuConfig {
    private static final Logger logger = LoggerFactory.getLogger(JibuConfig.class);
    private static String PROPERTIES_FILE = "jibu.properties";
    private static Properties prop;

    static {
        prop = new Properties();
        try {
            prop.load(JibuConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
            logger.info("Successfully loaded "+PROPERTIES_FILE+".");
        } catch (Exception e) {
            logger.error("Failed to load file: "+PROPERTIES_FILE+".", e);
        }
    }

    // 这个类就不用这样实例化了。:-)
    private JibuConfig() {}

    /**
     * Retrieve a property value
     * @param     key Name of the property
     * @return    String Value of property requested, null if not found
     */
    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

    /**
     * 得到 jibu 的当前配置。
     * @return Properties
     */
    public static Properties getProperties() {
        return prop;
    }
}