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
package org.gaixie.jibu.security.service.impl;

import com.google.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.cache.Cache;
import org.gaixie.jibu.utils.CacheUtils;
import org.gaixie.jibu.utils.ConnectionUtils;
import org.gaixie.jibu.security.dao.RoleDAO;
import org.gaixie.jibu.security.model.Authority;
import org.gaixie.jibu.security.model.Role;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of Role service
 */
public class RoleServiceImpl implements RoleService {
    Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleDAO roleDAO;

    @Inject public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public Role get(int id) {
        Connection conn = null;
	Role role = null;
        try {
            conn = ConnectionUtils.getConnection();
            role = roleDAO.get(conn, id);
        } catch(SQLException e) {
	    logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
        }
	return role;
    }

    public Role get(String name) {
        Connection conn = null;
	Role role = null;
        try {
            conn = ConnectionUtils.getConnection();
            role = roleDAO.get(conn, name);
        } catch(SQLException e) {
	    logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
        }
	return role;
    }

    public void addChild(Role role, Role parent) throws JibuException {
        Connection conn = null;
        try {
            conn = ConnectionUtils.getConnection();
	    roleDAO.saveChild(conn, role,parent);
            DbUtils.commitAndClose(conn);
        } catch(SQLException e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            // RoleServiceImpl.001 = The role existed.
            throw new JibuException("RoleServiceImpl.001");
        } 
    }

    public List<Role> getAll() {
        Connection conn = null;
	List<Role> roles = null;
        try {
            conn = ConnectionUtils.getConnection();
            roles = roleDAO.getAll(conn);
        } catch(SQLException e) {
	    logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
        }
	return roles;
    }

    public void bind(Role role, Authority auth) throws JibuException {
        Connection conn = null;
        try {
            conn = ConnectionUtils.getConnection();
	    roleDAO.bind(conn, role, auth);
            DbUtils.commitAndClose(conn);
	    // 从cache中删除此auth对应的roles
	    // 下次调用findByAuthority的时候装载
	    Cache cache = CacheUtils.getAuthCache();
	    cache.remove(auth.getValue());
        } catch(SQLException e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            // RoleServiceImpl.002 = Bind authority failed.
            throw new JibuException("RoleServiceImpl.002");
        } 
    }

    public List<Role> findByAuthority(Authority auth) {
        Connection conn = null;
	List<Role> roles = null;

        try {
            conn = ConnectionUtils.getConnection();
            roles = roleDAO.findByAuthority(conn,auth);
        } catch(SQLException e) {
	    logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
        }
	return roles;

    }

    public void bind(Role role, User user) throws JibuException {
        Connection conn = null;
        try {
            conn = ConnectionUtils.getConnection();
	    roleDAO.bind(conn,role,user);
            DbUtils.commitAndClose(conn);
	    // 从cache中删除此user对应的roles
	    // 下次调用findByUser的时候装载
	    Cache cache = CacheUtils.getUserCache();
	    cache.remove(user.getUsername());
        } catch(SQLException e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            // RoleServiceImpl.003 = Bind user failed.
	    throw new JibuException("RoleServiceImpl.003");
        } 
    }

    public List<Role> findByUser(User user) {
        Connection conn = null;
	List<Role> roles = null;
        try {
            conn = ConnectionUtils.getConnection();
            roles = roleDAO.findByUser(conn,user);
        } catch(SQLException e) {
	    logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
        }
	return roles;
    }
}
