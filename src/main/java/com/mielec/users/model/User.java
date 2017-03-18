package com.mielec.users.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "users")
@Table(name = "users")
public class User {

    private String username;
    private String password;
    private boolean enabled;
    private Float salary;
    private Set<UserRole> userRole = new HashSet<UserRole>(0);
    private Set<UserDeps> userDeps = new HashSet<UserDeps>(0);
    public User() {
    }

    public User(String username, String password, boolean enabled,Float salary) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.salary=salary;
    }

    public User(String username, String password,
                boolean enabled, String d_id,Float salary, Set<UserRole> userRole) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.salary=salary;
        this.userRole = userRole;
    }

    @Id
    @Column(name = "username", unique = true,
            nullable = false, length = 45)
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password",
            nullable = false, length = 60)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "enabled", nullable = false)
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Column(name = "salary", nullable = false)
    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    public Set<UserRole> getUserRole() {
        return this.userRole;
    }

    public void setUserRole(Set<UserRole> userRole) {
        this.userRole = userRole;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    public Set<UserDeps> getUserDeps() {
        return this.userDeps;
    }

    public void setUserDeps(Set<UserRole> userRole) {
        this.userDeps = userDeps;
    }

}