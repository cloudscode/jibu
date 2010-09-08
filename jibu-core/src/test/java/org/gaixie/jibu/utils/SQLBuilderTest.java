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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
    }

    @Test public void testBeanToDerby() throws Exception {
        // Where 语句测试
        Person p = BeanConverter.mapToBean(Person.class,map);
        String sql = SQLBuilder.beanToDerbyClause(p,"AND");
        sql = SQLBuilder.getWhereClause(sql);
        StringBuilder sb = new StringBuilder();
        sb.append("WHERE age = 34 AND \n");
        sb.append("      birthday = '2010-10-10 23:10:00' AND \n");
        sb.append("      hasChildren = 0 AND \n");
        sb.append("      id = 1 AND \n");
        sb.append("      married = 1 AND \n");
        sb.append("      name = '王大' AND \n");
        sb.append("      salary = 10000.89 ");
        Assert.assertTrue(sql.equals(sb.toString()));

        // 如果有属性值为空，不应该输出
        p.setName(null);
        sql = SQLBuilder.beanToDerbyClause(p,"AND");
        sql = SQLBuilder.getWhereClause(sql);
        sb = new StringBuilder();
        sb.append("WHERE age = 34 AND \n");
        sb.append("      birthday = '2010-10-10 23:10:00' AND \n");
        sb.append("      hasChildren = 0 AND \n");
        sb.append("      id = 1 AND \n");
        sb.append("      married = 1 AND \n");
        sb.append("      salary = 10000.89 ");
        Assert.assertTrue(sql.equals(sb.toString()));

        // SET 语句测试
        sql = SQLBuilder.beanToDerbyClause(p,",");
        sql = SQLBuilder.getSetClause(sql);
        sb = new StringBuilder();
        sb.append("SET   age = 34 , \n");
        sb.append("      birthday = '2010-10-10 23:10:00' , \n");
        sb.append("      hasChildren = 0 , \n");
        sb.append("      id = 1 , \n");
        sb.append("      married = 1 , \n");
        sb.append("      salary = 10000.89 ");
        Assert.assertTrue(sql.equals(sb.toString()));
        // 将原本为null 的name 属性置为 ""
        p.setName("");
        sql = SQLBuilder.beanToDerbyClause(p,",");
        sql = SQLBuilder.getSetClause(sql);
        sb = new StringBuilder();
        sb.append("SET   age = 34 , \n");
        sb.append("      birthday = '2010-10-10 23:10:00' , \n");
        sb.append("      hasChildren = 0 , \n");
        sb.append("      id = 1 , \n");
        sb.append("      married = 1 , \n");
        sb.append("      name = '' , \n");
        sb.append("      salary = 10000.89 ");
        Assert.assertTrue(sql.equals(sb.toString()));

    }
}
