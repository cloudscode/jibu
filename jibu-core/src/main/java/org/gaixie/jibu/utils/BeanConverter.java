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
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.model.Criteria;

/**
 * 提供静态方法将不同的对象与 Javabean 进行转化。
 *
 */
public class BeanConverter {

    /**
     * 通过 Map 对Javabean 进行实例化并赋值。
     * <p>
     * Map 对象的格式必须为 key/value 。如果 map 中的 key 与 javabean
     * 中的属性名称一致，并且值可以被转化，则进行赋值。<p>
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
        try {
            T bean = cls.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(cls);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

            for( PropertyDescriptor pd : pds ){
                Object obj = map.get(pd.getName());
                // 如果map中有相同的属性名
                if( null != obj ) {
                    Class type = pd.getPropertyType();
                    Object value = getBeanValue(type, obj);
                    if (null != value ) {
                        pd.getWriteMethod().invoke(bean,value);
                    }
                }
            }
            return bean;
        } catch (Exception e) {
            throw new JibuException(e.getMessage());
        }
    }

    private static Object getBeanValue(Class cls, Object value) throws JibuException {
        try {
            if (int.class.equals(cls)) {
                return Integer.parseInt(value.toString());
            } else if (Integer.class.equals(cls)) {
                return Integer.valueOf(value.toString());
            } else if (String.class.equals(cls)) {
                return value.toString();
            } else if (boolean.class.equals(cls)) {
                return Boolean.parseBoolean(value.toString());
            } else if (Boolean.class.equals(cls)) {
                return Boolean.valueOf(value.toString());
            } else if (float.class.equals(cls)) {
                return Float.parseFloat(value.toString());
            } else if (Float.class.equals(cls)) {
                return Float.valueOf(value.toString());
            } else if (Date.class.equals(cls)) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return format.parse(value.toString());
            }
        } catch (ParseException e) {
            throw new JibuException(value.toString()+" cant convert to "+cls.getName());
        }
        return null;
    }

    /**
     * 通过 Bean 实例转化为 Derby 的 SQL 语句，只转化非空属性。
     * <p>
     * 支持的属性类型有 int, Integer, float, Float, boolean, Boolean ,Date
     *
     * @param o Bean 实例
     * @param split sql 语句的分隔符，如 " AND " 或 " , "
     * @return Derby 的 SQl 语句
     *
     * @exception JibuException 转化失败时抛出
     */
    /*
      public static String beanToDerbySQL(Object o, String split) throws JibuException {
      StringBuilder sb = new StringBuilder();
      if(o == null) return null;
      try{
      PropertyDescriptor[] pds = Introspector.getBeanInfo(o.getClass() ).getPropertyDescriptors();
      for( int pdi = 0; pdi < pds.length; pdi ++ ){
      String name  = "      "+pds[pdi].getName();
      Object value = pds[pdi].getReadMethod().invoke( o );
      if (null == value || "class".equals(name) ||
      "serialVersionUID".equals(name)) continue;

      String stm = derbySQL(name
      ,pds[pdi].getPropertyType()
      ,value);
      if (null != stm) {
      sb.append(stm + split+" \n");
      }
      }
      } catch( Exception e){
      throw new JibuException(e.getMessage());
      }
      return sb.toString();
      }

      private static String derbySQL(String name, Class type, Object value) throws JibuException {
      try{
      if (String.class.equals(type)) {
      return (name +" = '"+(String)value+"' ");
      } else if (Date.class.equals(type)) {
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return (name +" = '"+format.format((Date)value)+"' ");
      } else if (boolean.class.equals(type)||Boolean.class.equals(type)) {
      return (name +" = "+(((Boolean)value)? "1":"0") +" ");
      } else if (int.class.equals(type)||Integer.class.equals(type)) {
      return (name +" = "+(Integer)value+" ");
      } else if (float.class.equals(type)||Float.class.equals(type)) {
      return (name +" = "+(Float)value+" ");
      }
      } catch( Exception e){
      throw new JibuException(e.getMessage());
      }
      return null;
      }

      /**
      * 整理 Where 语句，补充 Where 关键字，剔除多余的 And 关键字。
      * <p>
      *
      * @param sql 要处理的 Where 语句
      * @return 有效的 Where 语句
      *
      */
    /*
      public static String getWhereSQL (String sql) {
      if (null == sql ) return null;

      String s = sql.trim();
      int len = s.length();
      int last = s.lastIndexOf("AND");
      if (len > 0 && (last == len -3)) {
      if (s.indexOf("WHERE") == -1) {
      s = "WHERE " + s.substring(0,len-3);
      } else {
      s = s.substring(0,len-3);
      }
      }
      return s;
      }

      /**
      * 整理 SET 语句，补充 SET 关键字，剔除多余的 " , "。
      * <p>
      *
      * @param sql 要处理的 Where 语句
      * @return 有效的 Where 语句
      *
      */
    /*
      public static String getSetSQL (String sql) {
      if (null == sql ) return null;

      String s = sql.trim();
      int len = s.length();
      int last = s.lastIndexOf(",");
      if (len > 0 && (last == len -1)) {
      if (s.indexOf("SET") == -1) {
      s = "SET   " + s.substring(0,len-1);
      } else {
      s = s.substring(0,len-1);
      }
      }
      return s;
      }

      /**
      * 通过 criteria 生成的 Derby 分页 SQL 语句。
      * <p>
      * 必须在 WHERE 子句全部处理完以后条用。如果有 ORDER BY，也要在其之前调用。
      * Derby 10.5 以后版本有效。
      *
      * @param sql 要处理语句
      * @param crt 传递分页信息
      * @return 有效的 sql语句
      * @see #getWhereSQL(String sql)
      */
    /*
      public static String getPagingDerbySQL (String sql,Criteria crt) {
      if (null == sql ) return null;

      if (crt.getLimit()>0) {
      // 参考 ：http://db.apache.org/derby/docs/dev/ref/rrefsqljoffsetfetch.html
      return  sql  + " OFFSET "+crt.getStart()+" ROWS FETCH NEXT "+crt.getLimit()+" ROWS ONLY ";
      }
      return sql;
      }

      /**
      * 通过 criteria 生成排序的 sql 语句。数据库类型无关。
      * <p>
      * 注意，如果结合分页，要保证在分页语句之前条用。
      *
      * @param sql 要处理语句
      * @param crt 传递排序信息
      * @return 有效的 sql语句
      *

      public static String getSortSQL (String sql,Criteria crt) {
      if (null == sql ) return null;

      if (null != crt.getSort()) {
      return sql + " ORDER BY " + crt.getSort() + " "+crt.getDir();
      }
      return sql;
      }
    */
}
