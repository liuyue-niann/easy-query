## easy-query

🌤️简单的操作，极致的享受

😘开箱即用，五分钟极速入门，学习成本趋于0

### 快速启动

```sql
CREATE TABLE `users`
(
    `id`       int NOT NULL,
    `username` varchar(50)  DEFAULT NULL,
    `age`      int          DEFAULT NULL,
    `email`    varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
```

#### 创建实体

```java

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
```

#### 创建service

```java
public class UserService extends BaseMapper<User> {

}
```

#### 查询

```java
UserService userservice = new UserService();
userservice.

list();//查所有
userservice.

byid();//根据id查询
// 条件查询
userService.
                select().
                where().
                eq("id", 1)
                .and()
                .eq("username", "user1")
                .build().one();
//select * from users where id = 1 and username ='user1'
```
### 相关注解

- @Table: 实体类注解,定义实体表名
- @Id: 定义id字段
- @Field: 定义字段别名
  - 参数: required如果是false则不参与数据查询映射
- @ManyToOne: 多对一注解
- @OneToMany: 一对多注解
