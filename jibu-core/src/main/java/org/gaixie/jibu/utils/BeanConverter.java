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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.InvocationTargetException;

/**
 * 使用静态方法将不同的对象与javabean进行转化。
 *
 */
public class BeanConverter {

    /**
     * <p>通过Map 对Javabean 进行实例化并赋值，Map 对象的格式必须为 key/value 。
     * 如果map中的key与javabean中的属性名称一致，并且值可以被转化，则进行赋值</p>
     *
     * <p>Map中的value不能为数组类型，也就是说不能用request.getParameterValues()
     * 来获取value</p>
     *
     * @param cls 被处理bean的Class
     * @param map 由bean属性名做key的Map
     * @return 实例化，并赋值完成的Bean
     *
     * @exception IntrospectionException 如果获取类属性失败
     * @exception IllegalAccessException 如果实例化JavaBean失败，或者是map的value
     *  在转化为相应的bean属性时抛出，如将一个子母转化为Float类型。 
     * @exception InstantiationException 如果实例化JavaBean失败
     * @exception InvocationTargetException 如果调用属性的setter方法失败
     * @exception ParseException 在处理Date类型时的抛出的异常
     */
    public static <T> T mapToBean(Class<T> cls, Map map)
        throws IntrospectionException, IllegalAccessException, 
               InstantiationException, InvocationTargetException, ParseException {
        // 取Bean的属性
        BeanInfo beanInfo = Introspector.getBeanInfo(cls); 
        T bean = cls.newInstance();
        
        // 循环取出bean的每一个具体的属性
        for(PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()){
            String name = descriptor.getName();
            // 如果map中有相同的属性名
            if( map.containsKey(name)) {
                Class type = descriptor.getPropertyType();
                Object value = map.get(name);
                if (value == null ) continue;
                if (int.class.equals(type)) {
                    descriptor.getWriteMethod().invoke(bean, Integer.parseInt(value.toString()));
                }
                if (Integer.class.equals(type)) {
                    descriptor.getWriteMethod().invoke(bean, Integer.valueOf(value.toString()));
                }
                if (String.class.equals(type)) {
                    descriptor.getWriteMethod().invoke(bean, value.toString());
                }
                if (boolean.class.equals(type)) {
                    descriptor.getWriteMethod().invoke(bean, Boolean.parseBoolean(value.toString()));
                }
                if (Boolean.class.equals(type)) {
                    descriptor.getWriteMethod().invoke(bean, Boolean.valueOf(value.toString()));
                }
                if (float.class.equals(type)) {
                    descriptor.getWriteMethod().invoke(bean, Float.parseFloat(value.toString()));
                }
                if (Float.class.equals(type)) {
                    descriptor.getWriteMethod().invoke(bean, Float.valueOf(value.toString()));
                }
                if (Date.class.equals(type)) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    descriptor.getWriteMethod().invoke(bean, format.parse(value.toString()));
                }
            }
        }
        return bean;
    }
}
