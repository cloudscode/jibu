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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.gaixie.jibu.JibuException;
import org.gaixie.jibu.cache.Cache;
import org.gaixie.jibu.utils.CacheUtils;
import org.gaixie.jibu.utils.ConnectionUtils;
import org.gaixie.jibu.security.dao.AuthorityDAO;
import org.gaixie.jibu.security.dao.RoleDAO;
import org.gaixie.jibu.security.model.Authority;
import org.gaixie.jibu.security.model.Role;
import org.gaixie.jibu.security.model.User;
import org.gaixie.jibu.security.service.AuthorityService;
import org.gaixie.jibu.security.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Authority 服务接口的默认实现。
 * <p>
 */
public class AuthorityServiceImpl implements AuthorityService {
    Logger logger = LoggerFactory.getLogger(AuthorityServiceImpl.class);
    private final AuthorityDAO authDAO;
    private final RoleDAO roleDAO;

    /**
     * 使用 Guice 进行 DAO 的依赖注入。
     * <p>
     */
    @Inject public AuthorityServiceImpl(AuthorityDAO authDAO,
                                        RoleDAO roleDAO) {
        this.authDAO = authDAO;
        this.roleDAO = roleDAO;
    }

    public Authority get(int id) {
        Connection conn = null;
	Authority auth = null;
        try {
            conn = ConnectionUtils.getConnection();
            auth = authDAO.get(conn,id);
        } catch(SQLException e) {
	    logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
        }
	return auth;
    }

    public Authority get(String value, int mask) {
        Connection conn = null;
	Authority auth = null;
        try {
            conn = ConnectionUtils.getConnection();
            auth = authDAO.get(conn,value,mask);
        } catch(SQLException e) {
	    logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
        }
	return auth;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 此操作会清空 Cache 中 authorities 对应的 value，下次请求时装载。
     * @see #getAll()
     */
    public void add(Authority auth) throws JibuException {
        Connection conn = null;
        try {
            conn = ConnectionUtils.getConnection();
	    authDAO.save(conn,auth);
            DbUtils.commitAndClose(conn);
	    Cache cache = CacheUtils.getAuthCache();
	    cache.remove("authorities");
        } catch(SQLException e) {
            DbUtils.rollbackAndCloseQuietly(conn);
            // RoleServiceImpl.001 = The authority existed.
            throw new JibuException("AuthorityServiceImpl.001");
        } 
    }

    /**
     * {@inheritDoc}
     * <p>
     * 如果 Cache 中的 authorities 对应的 value 为空，从 DAO 得到最新的
     * value 并装入 Cache。
     */
    public List<Authority> getAll() {
        Connection conn = null;
	List<Authority> auths = null;

	Cache cache = CacheUtils.getAuthCache();
	auths = (List<Authority>)cache.get("authorities");
	if (null != auths) return auths;
        try {
            conn = ConnectionUtils.getConnection();
            auths = authDAO.getAll(conn);
	    cache.put("authorities",auths);
        } catch(SQLException e) {
	    logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
        }
	return auths;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 如果 Cache 中的 username 对应的 value 为空，从 DAO 得到最新的并装入
     * Cache。
     */
    public List<String> findRoleNamesByUsername(String username) {
        Connection conn = null;
	Cache cache = CacheUtils.getUserCache();
	List<String> names = (List<String>)cache.get(username);
	if (null != names) return names;

        try {
            conn = ConnectionUtils.getConnection();
            names = roleDAO.findByUsername(conn,username);
	    cache.put(username,names);
        } catch(SQLException e) {
	    logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
        }
	return names;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 初次调用会访问数据库，之后就访问 Cache，直到 Cache 更新。<br>
     * 将所有的 Authority 绑定的角色与 username 拥有的角色进行匹配，
     * 成功则将 Authority name 装入 List。
     */
    public List<String> findNamesByUsername(String username) {
	List<String> authNames = new ArrayList<String>();
	List<Authority> auths = getAll();
	List<String> roleNames = findRoleNamesByUsername(username);
	for (Authority auth : auths) {
            // 写死ROLE_ADMIN角色无须任何判断，加载所有数据
            if (roleNames.contains("ROLE_ADMIN")) {
		authNames.add(auth.getName());
		continue;
            }

	    Map<Integer,List<String>> map = findMapByValue(auth.getValue());
	    // 如果map为空，表示此auth不存在，并发修改产生？
            // map.size()==0 ，表示auth还没绑定任何Role，所有用户都有权限
            if (null == map) continue;
	    if (map.size()==0) {
		authNames.add(auth.getName());
		continue;
	    }
            boolean matched = false;
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()&& !matched) {
                Map.Entry entry = (Map.Entry) iter.next();
                Integer key = (Integer)entry.getKey();
                List<String> val = (List<String>)entry.getValue();
                for (String role : val) {
                    if (roleNames.contains(role)) {
                        authNames.add(auth.getName());
                        // 一个auth.value可能因为mask不同，有多组role
                        // 只要匹配任意一组，就可以在菜单上显示，break到最外层循环
                        matched = true;
                        break;
                    }
                }

            }
	}
	return authNames;
    }


    private Map<Integer,List<String>> findMapByValue(String value) {
        Connection conn = null;
	Cache cache = CacheUtils.getAuthCache();
	Map<Integer,List<String>> map = (HashMap<Integer,List<String>>)cache.get(value);
	if (null != map) return map;
        map = new HashMap<Integer,List<String>>();
        try {
            conn = ConnectionUtils.getConnection();
            // 先根据value取得一组authority(mask不同)，如果auth不存在，直接返回
            List<Authority> auths = authDAO.findByValue(conn,value);
            if (auths.size()==0) return null;
            // 不同的mask有一组roles
            for (Authority auth : auths) {
                List<String> roles = roleDAO.findByAuthid(conn,auth.getId());
                if (null != roles) {
                    map.put(auth.getMask(),roles);
                }
            }
	    cache.put(value,map);
        } catch(SQLException e) {
	    logger.error(e.getMessage());
        } finally {
            DbUtils.closeQuietly(conn);
        }
	return map;
    }

    /**
     * {@inheritDoc}
     * <p>
     * 初次调用会访问数据库，之后就访问 Cache，直到 Cache 更新。<br>
     * 权限按下面过程进行验证：
     * <ul>
     * <li> 首先根据 action 得到一个结构为{@code <mask,List<Role>>>}
     * 的 HashMap。</li>
     * <li> 用 crud 依次与 mask 进行比对(位操作)，如果成功取得{@code List<Role>}</li>
     * <li> 用 username 取得自己拥有的角色，与{@code List<Role>} 匹配，
     * 成功则返回 true，如果全部匹配失败，返回false。</li>
     * </ul><br>
     * 除了正常的权限验证，还有下面一些默认约定：
     * <ul>
     * <li>如果 username 没有绑定任何 Role，即没有任何权限，返回 false。</li>
     * <li>如果 username 有管理员角色 ROLE_ADMIN ，即拥有所有权限，返回 true。</li>
     * <li>通过 action 没有找到任何匹配的 Authority，表示该操作无效，
     * 返回 false。</li>
     * <li>如果匹配的 Authority 没有绑定任何 Role，认为它不受权限控制，
     * 返回 true。</li>
     * </ul>
     *
     */
    public boolean verify(String action, int crud, String username) {
        List<String> userRoles = findRoleNamesByUsername(username);
        if ( null == userRoles) return false;
        if (userRoles.contains("ROLE_ADMIN")) return true;

        Map<Integer,List<String>> map  = findMapByValue(action);
        // 要想暂时禁止所有用户访问，就只将此auth绑定到一个没有任何用户的角色上
        if ( null == map ) return false;
        if ( map.size()==0 ) return true;

        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Integer mask = (Integer)entry.getKey();
            // 位操作判断权限
            if (crud == (crud & mask)) {
                List<String> val = (List<String>)entry.getValue();
                for (String role: val) {
                    if(userRoles.contains(role)) {
                        return true;
                    }
                }
            }
        }
	return false;
    }
}
