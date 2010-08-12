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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;


import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.gaixie.jibu.utils.ConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行数据库脚本，初始化 Schema。
 * <p>
 *
 */
public class SchemaCreate {
    private static final Logger logger = LoggerFactory.getLogger(SchemaCreate.class);

    /**
     * 遍历 dbscripts 目录，并根据 type，按文件名顺序读取并执行相应目录的 sql 脚本文件。
     * <p>
     * @param type 数据库类型，应该从 jibu.properties 文件中取值。
     */
    public  void create(String type) {
        Connection conn = null;
        try {
            conn = ConnectionUtils.getConnection();
            DatabaseMetaData dbm = conn.getMetaData();

            ResultSet rs = dbm.getTables(null, null, "USERBASE", null);
            if (rs.next()) throw new SQLException("Schema has been created!");

            String dpn = dbm.getDatabaseProductName();
            if (!"Apache Derby".equals(dpn)) throw new SQLException("Database is not Apache Derby!");
            QueryRunner run = new QueryRunner();

            InputStream in = this.getClass().getResourceAsStream("/dbscripts/"+type+"/.");
            BufferedReader rdr = new BufferedReader(new InputStreamReader(in));
            String ln;
            while ((ln = rdr.readLine()) != null) {
                handleFile(run,conn,"/dbscripts/"+type+"/"+ln);
            }
            rdr.close();

            DbUtils.commitAndClose(conn);
        } catch (SQLException se) {
            DbUtils.rollbackAndCloseQuietly(conn);
            logger.warn("Schema create failed: "+ se.getMessage());
        } catch (IOException ie) {
            DbUtils.rollbackAndCloseQuietly(conn);
            logger.warn("Schema create failed: "+ ie.getMessage());
        }
    }

    private void handleFile(QueryRunner run
                            ,Connection conn
                            ,String filename) throws SQLException, IOException {
        StringBuilder command = new StringBuilder();
        InputStream is = this.getClass().getResourceAsStream(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            command = handleLine(command, line, run, conn);
        }
        reader.close();
        checkForMissingLineTerminator(command);
    }

    private StringBuilder handleLine(StringBuilder command 
                                     ,String line
                                     ,QueryRunner run
                                     ,Connection conn) throws SQLException {

        String trimmedLine = line.trim();
        if (isComment(trimmedLine)) {
            //System.out.println(command);
        } else if (trimmedLine.endsWith(";")) {
            command.append(line.substring(0, line.lastIndexOf(";")));
            command.append(" ");
            run.update(conn, command.toString()); 
            command.setLength(0);
        } else if (trimmedLine.length() > 0) {
            command.append(line);
            command.append(" \n");
        }
        return command;
    }

    private boolean isComment(String trimmedLine) {
        return trimmedLine.startsWith("//") || trimmedLine.startsWith("--");
    }

    private void checkForMissingLineTerminator(StringBuilder command) 
        throws SQLException {
        if (command != null && command.toString().trim().length() > 0) {
            throw new SQLException("Missing end-of-line terminator (;) => " + command);
        }
    }
}
