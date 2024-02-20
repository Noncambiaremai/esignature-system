package com.sys.esignature.entity;

import javax.persistence.*;

@Entity
@Table(name = "user")   // 指定表名为user
public class User {

    @Id // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增
    private Integer id;

    @Column(name = "user_id", nullable = false) // 映射实体属性与数据库字段的对应关系
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "role_id")
    private Boolean roleId;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    // get和set方法

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Boolean getRoleId() {
        return roleId;
    }

    public void setRoleId(Boolean roleId) {
        this.roleId = roleId;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }


}
