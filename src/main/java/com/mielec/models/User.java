package com.mielec.models;


import javax.persistence.*;

@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name = "username")
    private String username;
    private String password;
    private Boolean enabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}