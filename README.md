## easy-query

🌤️简单的操作，极致的享受

### 项目结构

```java
src
  ├─main
  │  ├─java
  │  │  └─com
  │  │      └─nn
  │  │          ├─annocation 相关注解
  │  │          ├─config  数据源配置
  │  │          ├─core
  │  │          │  ├─dml 
  │  │          │  ├─dql 查询处理器
  │  │          │  └─wrapper 条件构造器
  │  │          │      └─impl
  │  │          ├─entity 测试实体类(后期删除)
  │  │          ├─exception 异常处理
  │  │          ├─service 后期删除
  │  │          └─test 后期删除
  │  └─resources 
  └─test


```

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
