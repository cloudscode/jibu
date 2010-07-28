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
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.security.dao.UserDAO;
import org.gaixie.jibu.security.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UserDAO接口的Derby实现
 */
public class UserDAODerby implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAODerby.class);

    public User get( Connection conn, String username) throws JibuException {
        ResultSetHandler<User> h = new BeanHandler(User.class);
        QueryRunner run = new QueryRunner();
        User user = null;
        try {
            user =  run.query(conn
                              , "SELECT id,username,password,fullname,emailaddress,enabled FROM Userbase WHERE username=? "
                              , h
                              , username);
        } catch(SQLException e) {
            throw new JibuException("001B-0002");
        }
        return user;
    }

    public User login(Connection conn,String username, String password) throws JibuException {
        ResultSetHandler<User> h = new BeanHandler(User.class);
        QueryRunner run = new QueryRunner();
        User user = null;
        try {
            user =  run.query(conn
                              , "SELECT id,username,password,fullname,emailaddress,enabled FROM Userbase WHERE username=? and password=?"
                              , h
                              , username
                              , password);
        } catch(SQLException e) {
            throw new JibuException("001B-0002");
        }
        return user;
    }

    public void save(Connection conn, User user) throws JibuException {
        QueryRunner run = new QueryRunner();
        try {
            run.update(conn
                       , "INSERT INTO Userbase (fullname,username,password,emailaddress,enabled) values (?,?,?,?,?)"
                       , user.getFullname()
                       , user.getUsername()
                       , user.getPassword()
                       , user.getEmailaddress()
                       , user.isEnabled()); 
        } catch(SQLException e) {
            throw new JibuException("001B-0002");
        }

    }
}
