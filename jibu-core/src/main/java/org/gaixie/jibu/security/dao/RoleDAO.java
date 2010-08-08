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
import org.gaixie.jibu.security.model.Role;
import org.gaixie.jibu.security.model.User;

/**
 * Role DAO Interface
 */
public interface RoleDAO {

    /**
     * 通过id得到一个角色
     *
     * @param conn 一个有效的数据库链接。
     * @param id 角色id。
     * 
     * @throws SQLException
     * @return 角色
     */
    public Role get(Connection conn, int id) throws SQLException;

    /**
     * 通过Role.name得到一个角色
     *
     * @param conn 一个有效的数据库链接。
     * @param name Role.name。
     * 
     * @throws SQLException
     * @return Role
     */
    public Role get(Connection conn, String name) throws SQLException;

    /**
     * 为给定角色增加一个子角色
     *
     * @param conn 一个有效的数据库链接。
     * @param role 新增的角色。
     * @param parent 给定的父角色。
     *
     * @throws SQLException 
     */
    public void saveChild(Connection conn, Role role, Role parent) throws SQLException ;


    /**
     * 得到全部角色
     *
     * @param conn 一个有效的数据库链接。
     *
     * @throws SQLException 
     * @return 角色集合
     */
    public List<Role> getAll(Connection conn) throws SQLException ;

    /**
     * 将角色和权限资源进行绑定
     *
     * @param conn 一个有效的数据库链接。
     * @param role 角色
     * @param auth 权限资源
     *
     * @throws SQLException 
     */
    public void bind(Connection conn, Role role, Authority auth) throws SQLException;

    /**
     * 通过给定权限资源，得到所有已经与其绑定的角色
     *
     * @param conn 一个有效的数据库链接。
     * @param auth 权限资源
     *
     * @throws SQLException 
     * @return 角色集合
     */
    public List<Role> findByAuthority(Connection conn, Authority auth) throws SQLException;

    /**
     * 将角色和用户进行绑定
     *
     * @param conn 一个有效的数据库链接。
     * @param role 角色
     * @param user 用户
     *
     * @throws SQLException 
     */
    public void bind(Connection conn, Role role, User user) throws SQLException;

    /**
     * 通过给定用户，得到所有已经与其绑定的角色
     *
     * @param conn 一个有效的数据库链接。
     * @param user 用户
     *
     * @throws SQLException 
     * @return 角色集合
     */
    public List<Role> findByUser(Connection conn, User user) throws SQLException;

    /**
     * 通过authority id 取得匹配的Role name
     *
     * @param conn 一个有效的数据库链接。
     * @param id 权限资源id
     *
     * @throws SQLException 
     * @return 角色名集合
     */
    public List<String> findByAuthid(Connection conn, int id) throws SQLException;

    /**
     * 通过username 取得匹配的Role name
     *
     * @param conn 一个有效的数据库链接。
     * @param name username
     *
     * @throws SQLException 
     * @return 角色名集合
     */
    public List<String> findByUsername(Connection conn, String name) throws SQLException;
}
