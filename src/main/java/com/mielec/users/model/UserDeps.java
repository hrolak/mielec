package com.mielec.users.model;



import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "user_deps")
@Table(name = "user_deps",
        uniqueConstraints = @UniqueConstraint(
                columnNames = { "username", "d_id" }))
public class UserDeps{

    private Integer id;
    private User username;
    private String d_id;

    public UserDeps() {
    }

    public UserDeps(User user, String d_id) {
        this.username = user;
        this.d_id = d_id;
    }


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id",
            unique = true, nullable = false)
    public Integer getUserDepsId() {
        return this.id;
    }

    public void setUserDepsId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    public User getUser() {
        return this.username;
    }

    public void setUser(User user) {
        this.username = user;
    }

    @Column(name = "d_id", nullable = false, length = 3)
    public String getDep() {
        return this.d_id;
    }

    public void setDep(String d_id) {
        this.d_id = d_id;
    }

}