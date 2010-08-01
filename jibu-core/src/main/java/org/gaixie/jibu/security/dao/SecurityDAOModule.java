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

import com.google.inject.AbstractModule;

import org.gaixie.jibu.security.dao.UserDAO;
import org.gaixie.jibu.security.dao.impl.UserDAODerby;
import org.gaixie.jibu.security.dao.impl.UserDAOPgSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SecurityDAO 的DI模块，根据 jibu.properties中的databaseType进行初始化。
 * 默认为 Derby， 如果使用Derby数据库，会在DI时，自动创建Schema。 
 */
public class SecurityDAOModule extends AbstractModule {
    private static final Logger logger = LoggerFactory.getLogger(SecurityDAOModule.class);
    private final String databaseType;

    public SecurityDAOModule() {
        this("Derby");
    }

    public SecurityDAOModule(String databaseType) {
        this.databaseType = databaseType;
    }


    @Override protected void configure() {
        if ("Derby".equalsIgnoreCase(databaseType)){
            SchemaCreate sc = new SchemaCreate();
            sc.create();
            bind(UserDAO.class).to(UserDAODerby.class);
        }
        if ("PostgreSQL".equalsIgnoreCase(databaseType)){
            bind(UserDAO.class).to(UserDAOPgSQL.class);
        }
    }
}