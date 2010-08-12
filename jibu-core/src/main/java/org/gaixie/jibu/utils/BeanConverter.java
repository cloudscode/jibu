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
import org.gaixie.jibu.JibuException;

/**
 * 提供静态方法将不同的对象与 Javabean 进行转化。
 *
 */
public class BeanConverter {

    /**
     * 通过 Map 对Javabean 进行实例化并赋值。
     * <p>
     * Map 对象的格式必须为 key/value 。如果 map 中的 key 与 javabean
     * 中的属性名称一致，并且值可以被转化，则进行赋值。<br>
     * 这里 map 中的 key 必须 类名.属性名 的格式，如 User.id<p>
     *
     * Map 中的 value 不能为数组类型，也就是说不能用
     * request.getParameterValues() 来获取 value。<p>
     *
     * @param cls 被处理 bean 的 Class
     * @param map 由 bean 属性名做 key 的 Map
     * @return 实例化，并赋值完成的 Bean
     *
     * @exception JibuException 转化失败时抛出
     */
    public static <T> T mapToBean(Class<T> cls, Map map) throws JibuException {
        T bean;
        try {
            bean = cls.newInstance();
            String cn = getClassName(cls);

            // 循环取出bean的每一个具体的属性
            PropertyDescriptor[] pds = 
                Introspector.getBeanInfo( cls ).getPropertyDescriptors();
            for( int pdi = 0; pdi < pds.length; pdi ++ ){
                String name = cn+"."+pds[pdi].getName();
                // 如果map中有相同的属性名
                if( map.containsKey(name)) {
                    Class type = pds[pdi].getPropertyType();
                    Object value = map.get(name);
                    if (value == null ) continue;
                    if (int.class.equals(type)) {
                        pds[pdi].getWriteMethod().invoke(bean, Integer.parseInt(value.toString()));
                    }
                    if (Integer.class.equals(type)) {
                        pds[pdi].getWriteMethod().invoke(bean, Integer.valueOf(value.toString()));
                    }
                    if (String.class.equals(type)) {
                        pds[pdi].getWriteMethod().invoke(bean, value.toString());
                    }
                    if (boolean.class.equals(type)) {
                        pds[pdi].getWriteMethod().invoke(bean, Boolean.parseBoolean(value.toString()));
                    }
                    if (Boolean.class.equals(type)) {
                        pds[pdi].getWriteMethod().invoke(bean, Boolean.valueOf(value.toString()));
                    }
                    if (float.class.equals(type)) {
                        pds[pdi].getWriteMethod().invoke(bean, Float.parseFloat(value.toString()));
                    }
                    if (Float.class.equals(type)) {
                        pds[pdi].getWriteMethod().invoke(bean, Float.valueOf(value.toString()));
                    }
                    if (Date.class.equals(type)) {
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        pds[pdi].getWriteMethod().invoke(bean, format.parse(value.toString()));
                    }
                }
            }
        } catch (Exception e) {
            throw new JibuException(e.getMessage());
        }
        return bean;
    }

    /**
     * 通过 Bean 实例转化为 Derby 的 SQL 查询条件语句，只转化非空属性。
     * <p>
     * 支持的属性类型有 int, Integer, float, Float, boolean, Boolean ,Date 
     *
     * @param o Bean 实例
     * @return Derby 的 SQl 语句
     *
     * @exception JibuException 转化失败时抛出
     */
    public static String beanToDerbySQL(Object o) throws JibuException {
        StringBuilder sb = new StringBuilder();
        if(o == null) return null;
        try{
            PropertyDescriptor[] pds = Introspector.getBeanInfo(o.getClass() ).getPropertyDescriptors();
            for( int pdi = 0; pdi < pds.length; pdi ++ ){
                Class type = pds[pdi].getPropertyType();

                String name = "      "+pds[pdi].getName();
                Object value = pds[pdi].getReadMethod().invoke( o );

                if (null == value || "class".equals(name) ||
                    "serialVersionUID".equals(name)) continue;
                if (String.class.equals(type)) {
                    sb.append(name +" = '"+(String)value+"' and \n");
                } else if (Date.class.equals(type)) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sb.append(name +" = '"+format.format((Date)value)+"' and \n");
                } else if (boolean.class.equals(type)||Boolean.class.equals(type)) {
                    sb.append(name +" = "+(((Boolean)value)? "1":"0") +" and \n");
                } else if (int.class.equals(type)||Integer.class.equals(type)) {
                    sb.append(name +" = "+(Integer)value+" and \n");
                } else if (float.class.equals(type)||Float.class.equals(type)) {
                    sb.append(name +" = "+(Float)value+" and \n");
                }

            }
        } catch( Exception e){
            throw new JibuException(e.getMessage());
        }
        return sb.toString();
    }

    /**
     * 整理 Where 语句，补充 Where 关键字，剔除多余的 And 关键字。
     * <p>
     *
     * @param sql 要处理的 Where 语句
     * @return 有效的 Where 语句
     *
     * @exception JibuException 转化失败时抛出
     */
    public static String getWhereSQL (String sql) {
        if (null == sql ) return null;

        String s = sql.trim();
        int len = s.length();
        int last = s.lastIndexOf("and");
        if (len > 0 && (last == len -3)) {
            if (s.indexOf("where") == -1) {
                s = "where " + s.substring(0,len-3);
            } else {
                s = s.substring(0,len-3);
            }
        }
        return s;
    }

    /*
     * 取 Class Name，剔除 Package。
     */
    private static String getClassName(Class cls) {
        String name = cls.getName();
        int firstChar;
        firstChar = name.lastIndexOf ('.') + 1;
        if ( firstChar > 0 ) {
            name = name.substring ( firstChar );
        }
        return name;
    }
}
