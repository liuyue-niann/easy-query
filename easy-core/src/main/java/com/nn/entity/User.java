package com.nn.entity;

import com.nn.annocation.Id;
import com.nn.annocation.Table;


@Table("users")
public class User {

    @Id
    Integer id;
    String username;
    Integer age;

    public Integer getId() {
        return id;
    }



    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
