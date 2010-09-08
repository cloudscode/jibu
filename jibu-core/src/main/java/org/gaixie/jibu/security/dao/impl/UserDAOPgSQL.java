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
package org.gaixie.jibu.security.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.gaixie.jibu.security.dao.UserDAO;
import org.gaixie.jibu.security.model.Criteria;
import org.gaixie.jibu.security.model.Authority;
import org.gaixie.jibu.security.model.Role;
import org.gaixie.jibu.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User 数据访问接口的 PostgreSQL 实现。
 * <p>
 */
public class UserDAOPgSQL implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOPgSQL.class);

    public User get(Connection conn, int id) throws SQLException {
        return null;
    }

    public User get(Connection conn, String username) throws SQLException {
        return null;
    }

    public User login(Connection conn,String username, String password) throws SQLException {
        return null;
    }

    public void save(Connection conn, User user) throws SQLException {

    }

    public void update(Connection conn, User user) throws SQLException {

    }

    public void delete(Connection conn, User user) throws SQLException {

    }

    public List<User> find( Connection conn, User user) throws SQLException {
        return null;
    }

    public int getTotal(Connection conn, User user) throws SQLException {
        return 0;
    }

    public List<User> find(Connection conn, User user, Criteria criteria) 
        throws SQLException {
        return null;
    }

    public List<User> find( Connection conn, Role role) throws SQLException {
        return null;
    }

    public List<User> find( Connection conn, Authority auth) throws SQLException {
        return null;
    }
}
