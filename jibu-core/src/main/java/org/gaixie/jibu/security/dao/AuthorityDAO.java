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
 * Authority 数据访问对象接口。
 */
public interface AuthorityDAO {

    /**
     * 通过 Authority Id 得到一个 Authority。
     *
     * @param conn 一个有效的数据库链接。
     * @param id Authority id。
     * 
     * @throws SQLException
     * @return Authority
     */
    public Authority get(Connection conn, int id) throws SQLException;

    /**
     * 通过 Authority value 和 mask 得到一个 Authority。
     *
     * @param conn 一个有效的数据库链接。
     * @param value Authority value。
     * @param mask Authority mask。
     * 
     * @throws SQLException
     * @return 权限对象
     */
    public Authority get(Connection conn, String value, int mask) throws SQLException;

    /**
     * 增加一个 Authority。
     *
     * @param conn 一个有效的数据库链接。
     * @param auth Authority。
     *
     * @throws SQLException 
     */
    public void save(Connection conn, Authority auth) throws SQLException;


    /**
     * 得到所有 Authority。
     *
     * @param conn 一个有效的数据库链接。
     *
     * @throws SQLException 
     * @return Authority List
     */
    public List<Authority> getAll(Connection conn) throws SQLException;

    /**
     * 根据 Authority value 查询 Authority。
     *
     * @param conn 一个有效的数据库链接。
     * @param value Authority value。
     *
     * @throws SQLException 
     * @return Authority List
     */
    public List<Authority> findByValue(Connection conn,String value) throws SQLException;

    /**
     * 删除 Authority 以及相关联的依赖关系。
     *
     * @param conn 一个有效的数据库链接。
     * @param auth 需要删除的 Authority。
     *
     * @throws SQLException 
     */
    public void delete(Connection conn, Authority auth) throws SQLException;

    /**
     * 更新除 id 以外的所有 Authority 属性。
     *
     * @param conn 一个有效的数据库链接。
     * @param auth 需要更新的 Authority。
     *
     * @throws SQLException 
     */
    public void update(Connection conn, Authority auth) throws SQLException;

    /**
     * 根据 Authority name 模糊查询( like 'name%') Authority 。
     *
     * @param conn 一个有效的数据库链接。
     * @param name Authority name。
     *
     * @throws SQLException 
     * @return Authority List
     */
    public List<Authority> findByName(Connection conn,String name) throws SQLException;

}
