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
package org.gaixie.jibu.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAO 接口的DBCP连接池实现
 */
public class ConnectionUtils {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionUtils.class);
    private static String PROPERTIES_FILE = "dbcp.properties";
    private static DataSource dataSource = null;

    private static synchronized DataSource getDataSource() {
        if(dataSource != null){
            return dataSource;
        }
        Properties prop = new Properties();
        try {
            prop.load(ConnectionUtils.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
            dataSource = BasicDataSourceFactory.createDataSource(prop); 
        } catch (IOException ioe) {
            logger.error("Failed to load file: "+PROPERTIES_FILE, ioe);
        } catch (Exception e) {
            logger.error("Create DataSource failed.", e);
        }
        return dataSource;
    }

    public static void printPoolStats() {
        BasicDataSource bds = (BasicDataSource) getDataSource();
        logger.debug("NumActive = " + bds.getNumActive()+" ; NumIdle = " + bds.getNumIdle());
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        conn = getDataSource().getConnection();
        return conn;
    }

    public static void setDefaultAutoCommit(boolean defaultAutoCommit) {
        BasicDataSource bds = (BasicDataSource) getDataSource();
        bds.setDefaultAutoCommit(defaultAutoCommit);
    }
}
