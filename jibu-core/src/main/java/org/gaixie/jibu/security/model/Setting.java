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
 * 系统配置模型类。
 */
public class Setting implements Comparable {

    private Integer id;
    private String name;
    private String value;
    private Integer sortindex;
    private Boolean enabled;

    /**
     * No-arg constructor.
     */
    public Setting() {

    }

    /**
     * Full constructor
     */
    public Setting(String name,String value,Integer sortindex,Boolean enabled) {
        this.name = name;
        this.value = value;
        this.sortindex = sortindex;
        this.enabled = enabled;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Accessor Methods ~~~~~~~~~~~~~~~~~~~~~~~~~~//
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public Integer getSortindex() { return sortindex; }
    public void setSortindex(Integer sortindex) { this.sortindex = sortindex; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Setting)) return false;
        final Setting setting = (Setting) o;
        return getName().equals(setting.getName()) && getValue().equals(setting.getValue()) ;
    }

    public String toString() {
        return  "Setting (" + getId() + "), Name: '" + getName() + "', Value: '" + getValue() + "'";
    }

    public int compareTo(Object o) {
        if (o instanceof Setting) {
            final Setting setting = (Setting) o;
            int i = this.getName().compareTo(setting.getName());
            if (i==0){
                return this.getSortindex().compareTo(setting.getSortindex());
            }else
                return i;
        }
        return 0;
    }
}
