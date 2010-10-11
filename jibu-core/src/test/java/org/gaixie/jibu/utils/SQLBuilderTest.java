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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.gaixie.jibu.config.JibuConfig;
import org.gaixie.jibu.utils.BeanConverter;
import org.gaixie.jibu.utils.Person;
import org.gaixie.jibu.utils.SQLBuilder;

public class SQLBuilderTest {
    private Map<Object,Object> map;

    @Before public void setup() throws Exception {
        map = new HashMap<Object,Object>();
        map.put("id","1");
        map.put("name","王大");
        map.put("age","34");
        map.put("married","true");
        map.put("hasChildren","false");
        map.put("salary","10000.89");
        map.put("birthday","2010-10-10 23:10:00");
        map.put("deathtime","2011-10-10 23:10:00");
    }

    @Test public void testBeanToSQL() throws Exception {
        // Where 语句测试
        Person p = BeanConverter.mapToBean(Person.class,map);
        String sql = SQLBuilder.beanToSQLClause(p,"AND");
        sql = SQLBuilder.getWhereClause(sql);
        StringBuilder sb = new StringBuilder();
        sb.append("WHERE age = 34 AND \n");
        sb.append("      "+getDate("birthday",p.getBirthday())+" AND \n");
        sb.append("      "+getTime("deathtime",p.getDeathtime())+" AND \n");
        sb.append("      "+getBoolean("hasChildren",p.getHasChildren())+" AND \n");
        sb.append("      id = 1 AND \n");
        sb.append("      "+getBoolean("married",p.isMarried())+" AND \n");
        sb.append("      name = '王大' AND \n");
        sb.append("      salary = 10000.89 ");
        Assert.assertTrue(sql.equals(sb.toString()));

        // 如果有属性值为空，不应该输出
        p.setName(null);
        sql = SQLBuilder.beanToSQLClause(p,"AND");
        sql = SQLBuilder.getWhereClause(sql);
        sb = new StringBuilder();
        sb.append("WHERE age = 34 AND \n");
        sb.append("      "+getDate("birthday",p.getBirthday())+" AND \n");
        sb.append("      "+getTime("deathtime",p.getDeathtime())+" AND \n");
        sb.append("      "+getBoolean("hasChildren",p.getHasChildren())+" AND \n");
        sb.append("      id = 1 AND \n");
        sb.append("      "+getBoolean("married",p.isMarried())+" AND \n");
        sb.append("      salary = 10000.89 ");
        Assert.assertTrue(sql.equals(sb.toString()));

        // SET 语句测试
        sql = SQLBuilder.beanToSQLClause(p,",");
        sql = SQLBuilder.getSetClause(sql);
        sb = new StringBuilder();
        sb.append("SET   age = 34 , \n");
        sb.append("      "+getDate("birthday",p.getBirthday())+" , \n");
        sb.append("      "+getTime("deathtime",p.getDeathtime())+" , \n");
        sb.append("      "+getBoolean("hasChildren",p.getHasChildren())+" , \n");
        sb.append("      id = 1 , \n");
        sb.append("      "+getBoolean("married",p.isMarried())+" , \n");
        sb.append("      salary = 10000.89 ");
        Assert.assertTrue(sql.equals(sb.toString()));
        // 将原本为null 的name 属性置为 ""
        p.setName("");
        sql = SQLBuilder.beanToSQLClause(p,",");
        sql = SQLBuilder.getSetClause(sql);
        sb = new StringBuilder();
        sb.append("SET   age = 34 , \n");
        sb.append("      "+getDate("birthday",p.getBirthday())+" , \n");
        sb.append("      "+getTime("deathtime",p.getDeathtime())+" , \n");
        sb.append("      "+getBoolean("hasChildren",p.getHasChildren())+" , \n");
        sb.append("      id = 1 , \n");
        sb.append("      "+getBoolean("married",p.isMarried())+" , \n");
        sb.append("      name = '' , \n");
        sb.append("      salary = 10000.89 ");
        Assert.assertTrue(sql.equals(sb.toString()));
    }

    private String getBoolean(String name, boolean value){
        String databaseType = JibuConfig.getProperty("databaseType");
        if("Derby".equals(databaseType) 
           || "MySQL".equals(databaseType) 
           || "Oracle".equals(databaseType)) {
            return name +" = "+((value)? "1":"0");
        } else if("PostgreSQL".equals(databaseType)) {
            return name +" = "+value;
        }
        return "";
    }

    private String getDate(String name, Date value){
        String databaseType = JibuConfig.getProperty("databaseType");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if("Derby".equals(databaseType) || "MySQL".equals(databaseType)) {
            return name +" = '"+format.format(value)+"'";
        } else if("PostgreSQL".equals(databaseType) || "Oracle".equals(databaseType)) {
            return name +" = to_date('"+format.format(value)+"','YYYY-MM-DD')";
        }
        return "";
    }

    private String getTime(String name, Timestamp value){
        String databaseType = JibuConfig.getProperty("databaseType");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if("Derby".equals(databaseType) || "MySQL".equals(databaseType)) {
            return name +" = '"+format.format(value)+"'";
        } else if("PostgreSQL".equals(databaseType) || "Oracle".equals(databaseType)) {
            return name +" = to_timestamp('"+format.format(value)+"','YYYY-MM-DD HH24:MI:SS')";
        }
        return "";
    }
}
