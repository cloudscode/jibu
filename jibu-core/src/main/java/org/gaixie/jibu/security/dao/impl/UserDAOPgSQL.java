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

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.gaixie.jibu.security.dao.UserDAO;
import org.gaixie.jibu.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UserDAO接口的PostgreSQL实现
 */
public class UserDAOPgSQL implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOPgSQL.class);
    private Connection conn = null;

    public UserDAOPgSQL(Connection conn) {
	this.conn = conn;
    }

    public User get(String username) throws SQLException {
            ResultSetHandler<User> h = new BeanHandler(User.class);
            QueryRunner run = new QueryRunner();
            return run.query(conn
                             , "SELECT id,username,password,fullname,emailaddress,enabled FROM Userbase WHERE username=? "
                             , h
                             , username);
    }

    public User login(String username, String password) throws SQLException {
        return null;
    }

    public void save(User user) throws SQLException {
            QueryRunner run = new QueryRunner();
            run.update(conn
                       , "INSERT INTO Userbase (fullname,username,password,emailaddress,enabled) values (?,?,?,?,?)"
                       , user.getFullname()
                       , user.getUsername()
                       , user.getPassword()
                       , user.getEmailaddress()
                       , user.isEnabled()); 
    }
}
