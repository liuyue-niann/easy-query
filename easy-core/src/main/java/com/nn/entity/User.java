package com.nn.entity;

import com.nn.annocation.Field;
import com.nn.annocation.Id;
import com.nn.annocation.Table;

import java.lang.annotation.Target;


@Table("users")
public class User {
    @Id
    Integer id;
    String username;

    Integer age;

    @Field(value = "pwd", required = false)
    String password;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", age=" + age +
                '}';
    }
}
