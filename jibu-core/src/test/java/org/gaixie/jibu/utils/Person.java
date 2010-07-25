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
package org.gaixie.jibu.utils;

import java.util.Date;
/*
 * 此Bean只用于BeanConverter的测试
 */ 
public class Person {
    
    private Integer id;
    private String name;
    private int age;
    private boolean married = false;
    private Boolean hasChildren;
    private float salary;
    private Date birthday;
    
    public Person() {
    }

    public Person(String name,int age,boolean married) {
        this.name = name;
        this.age = age;  
        this.married = married;  
    }
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public void setMarried(boolean married) { this.married = married; }
    public boolean isMarried() { return married; }

    public void setHasChildren(Boolean hasChildren) { this.hasChildren = hasChildren; }
    public Boolean isHasChildren() { return hasChildren; }

    public float getSalary() { return salary; }
    public void setSalary(float salary) { this.salary = salary; }

    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }

}
