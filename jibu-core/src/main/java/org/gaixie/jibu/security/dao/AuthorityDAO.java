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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.gaixie.jibu.security.model.Authority;

/**
 * Authority DAO Interface
 */
public interface AuthorityDAO {

    /**
     * 通过权限Id得到一个权限对象
     *
     * @param conn 一个有效的数据库链接。
     * @param id 权限id。
     * 
     * @throws SQLException
     * @return 权限对象
     */
    public Authority get(Connection conn, int id) throws SQLException;

    /**
     * 通过权限value和mask得到一个权限
     *
     * @param conn 一个有效的数据库链接。
     * @param value value。
     * @param mask 权限掩码。
     * 
     * @throws SQLException
     * @return 权限对象
     */
    public Authority get(Connection conn, String value, int mask) throws SQLException;

    /**
     * 增加一个权限
     *
     * @param conn 一个有效的数据库链接。
     * @param auth 权限对象。
     *
     * @throws SQLException 
     */
    public void save(Connection conn, Authority auth) throws SQLException;


    /**
     * 根据权限类型查询相关权限
     *
     * @param conn 一个有效的数据库链接。
     * @param type 权限类型。
     *
     * @throws SQLException 
     * @return 权限对象集合
     */
    public List<Authority> findByType(Connection conn,String type) throws SQLException;

    /**
     * 根据权限类型查询相关权限
     *
     * @param conn 一个有效的数据库链接。
     * @param value Authority.value。
     *
     * @throws SQLException 
     * @return Authority List
     */
    public List<Authority> findByValue(Connection conn,String value) throws SQLException;
}
