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
import org.junit.Test;
import org.gaixie.jibu.utils.Person;
import org.gaixie.jibu.utils.BeanConverter;

public class BeanConverterTest {

    @Test 
        public void testMapToBean() throws Exception {
        Map<Object,Object> map = new HashMap<Object,Object>();
        map.put("id","1");
        map.put("name","王大");
        map.put("age","34");
        map.put("married",true);
        map.put("hasChildren","false");
        map.put("salary","10000.89");
        map.put("birthday","2010-10-10 23:10:00");
        Person p = BeanConverter.mapToBean(Person.class,map);
        Assert.assertNotNull(p);
        Assert.assertTrue("王大".equals(p.getName()));
        Assert.assertTrue(34 == p.getAge());
        Assert.assertTrue(true == p.isMarried());
        Assert.assertTrue(false == p.isHasChildren());
        Assert.assertTrue(1 == p.getId());
        Assert.assertTrue(10000.89f == p.getSalary());
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Assert.assertEquals(p.getBirthday(), format.parse("2010-10-10 23:10:00"));
    }
}
