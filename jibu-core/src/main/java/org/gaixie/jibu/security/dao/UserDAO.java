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
import java.util.List;

import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.model.User;

/**
 * User DAO Interface
 */
public interface UserDAO {

    /**
     * 通过用户名得到一个用户对象
     *
     * @param conn 一个有效的数据库链接。
     * @param username 用户的登录名。
     * 
     * @throws SQLException bean封装时有错误时抛出。
     * @return 用户对象
     */
    public User get(Connection conn, String username) throws JibuException;

    /**
     * 通过用户名及密码得到一个用户对象
     *
     * @param conn 一个有效的数据库链接。
     * @param username 用户的登录名。
     * @param password hash后的用户登录密码。
     *
     * @throws SQLException bean封装时有错误时抛出。
     * @return 用户对象
     */
    public User login(Connection conn, String username, String password) throws JibuException;


    /**
     * 增加一个新用户
     *
     * @param conn 一个有效的数据库链接。
     * @param user 用户对象。
     *
     * @throws JibuException 如果用户名重复，会抛出此异常。
     */
    public void save(Connection conn, User user) throws JibuException;


    /**
     * 根据用户属性查询符合条件的用户集合
     *
     * @param conn 一个有效的数据库链接。
     * @param user 用户对象，属性值会作为查询匹配的条件。
     *
     * @throws JibuException 数据库执行发生异常
     */
    public List<User> find(Connection conn, User user) throws JibuException;

}
