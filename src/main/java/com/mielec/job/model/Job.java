package com.mielec.job.model;


import com.mielec.users.model.UserRole;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "job")
@Table(name = "job")
public class Job {

    private int id;
    private String user_id;
    private int project_id;
    private int time;


    public Job() {
    }

    public Job(String user_id,int project_id,int time) {
        this.id=id;
        this.user_id=user_id;
        this.project_id=project_id;
        this.time=time;
    }

    @Id
    @Column(name = "id", unique = true,
            nullable = false, length = 30)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id=id;
    }

    @Column(name = "user_id",
            nullable = false, length = 30)
    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id=user_id;
    }

    @Column(name = "project_id",
            nullable = false, length = 30)
    public int getProject_id() {
        return this.project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id=project_id;
    }

    @Column(name = "time",
            nullable = false, length = 30)
    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time=time;
    }

}