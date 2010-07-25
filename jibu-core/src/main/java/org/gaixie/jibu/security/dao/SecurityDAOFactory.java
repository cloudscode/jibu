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
package org.gaixie.jibu.security.dao;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import org.gaixie.jibu.security.dao.*;
import org.gaixie.jibu.security.dao.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class SecurityDAOFactory {
    private static final Logger logger = LoggerFactory.getLogger(SecurityDAOFactory.class);
    private static String PROPERTIES_FILE = "jibu.properties";
    private String databaseType =  "Derby";
    private static SecurityDAOFactory instance;


    private SecurityDAOFactory() {
        Properties prop = new Properties();
        try {
            prop.load(SecurityDAOFactory.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
            if(prop.containsKey("databaseType")) 
                databaseType =  prop.getProperty("databaseType");
        } catch (IOException ioe) {
            logger.error("Failed to load file: "+PROPERTIES_FILE, ioe);
        }
    }


    static {
        instance = new SecurityDAOFactory();
    }

    public static SecurityDAOFactory getInstance() {
        return instance;
    }

    public UserDAO getUserDAO(Connection conn) {
        if ("Derby".equalsIgnoreCase(databaseType)){
            return new UserDAODerby(conn);
        }
        if ("PostgreSQL".equalsIgnoreCase(databaseType)){
            return new UserDAOPgSQL(conn);
        }
        return null;
    }

}