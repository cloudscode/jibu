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

/**
 * 继承型角色模型类。
 * <p>
 */
public class Role {
    
    private Integer id;
    private String name;
    private String description;
    private int lft;
    private int rgt;

    /**
     * No-arg constructor.
     */
    public Role() {
    }

    /**
     * Simple constructor
     */
    public Role(String name,String description) {
        this.name = name;
        this.description = description;
     }
    

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Accessor Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~//    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getLft() { return lft; }
    public void setLft(int lft) { this.lft = lft; }

    public int getRgt() { return rgt; }
    public void setRgt(int rgt) { this.rgt = rgt; }

    // ********************** Common Methods ********************** //
    
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        final Role role = (Role) o;
        return getName().equals(role.getName());
    }
}
