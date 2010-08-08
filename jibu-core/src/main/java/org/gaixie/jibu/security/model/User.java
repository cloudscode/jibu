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
package org.gaixie.jibu.security.model;

public class User {
    
    private Integer id;
    private String fullname;
    private String username;
    private String password;
    private String emailaddress;
    private boolean enabled=true;
    
    /**
     * No-arg constructor for JavaBean tools.
     */
    public User() {
        
    }

    /**
     * Simple constructor
     */
    public User(String fullname,String username,String password) {
        this.fullname = fullname;
        this.username = username;     
        this.password = password;  
    }
    
    /**
     * Full constructor
     */
    public User(String fullname,String username,String password,String emailaddress,boolean enabled) {
        this.fullname = fullname;
        this.username = username;     
        this.password = password;  
        this.emailaddress = emailaddress;  
        this.enabled = enabled;  
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Accessor Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~//    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmailaddress() { return emailaddress; }
    public void setEmailaddress(String emailaddress) { this.emailaddress = emailaddress; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isEnabled() { return enabled; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        final User user = (User) o;
        return getUsername().equals(user.getUsername());
    }

    public int hashCode() {
        return getUsername().hashCode();
    }
}
