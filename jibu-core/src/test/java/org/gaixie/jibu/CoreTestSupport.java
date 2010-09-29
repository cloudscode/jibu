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
package org.gaixie.jibu;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.gaixie.jibu.config.JibuConfig;
import org.gaixie.jibu.security.dao.SecurityDAOModule;
import org.gaixie.jibu.security.service.SecurityServiceModule;


/**
 * 需要Guice注入的测试类的父类，使测试类取得injector，通过同步化保证injector只被创建一
 * 次，同时通过读取jibu.properties文件的databaseType，使DAO的测试类可以无须修改就
 * 测试多种类型的数据库。
 */
public class CoreTestSupport {
    private static Injector injector;

    public synchronized Injector getInjector() {
        if(injector != null){
            return injector;
        }
        String databaseType = JibuConfig.getProperty("databaseType");
        injector = Guice.createInjector(new SecurityServiceModule(),
                                            new SecurityDAOModule(databaseType));
        return injector;
    }
}
