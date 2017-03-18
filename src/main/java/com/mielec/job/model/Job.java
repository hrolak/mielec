package com.mielec.job.model;


import com.mielec.users.model.UserRole;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
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
    private double time;
    private Date date;
    private String d_id;
    public Job() {
    }

    public Job(String user_id,int project_id,double time,Date date,String d_id) {
        this.user_id=user_id;
        this.project_id=project_id;
        this.time=time;
        this.date=date;
        this.d_id=d_id;
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
    public double getTime() {
        return this.time;
    }

    public void setTime(double time) {
        this.time=time;
    }

    @Column(name = "date",
            nullable = false, length = 30)
    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date=date;
    }

    @Column(name = "d_id",
            nullable = false, length = 3)
    public String getDep() {
        return this.d_id;
    }

    public void setDep(String d_id) {
        this.d_id=d_id;
    }

}