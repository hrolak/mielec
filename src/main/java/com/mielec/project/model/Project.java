package com.mielec.project.model;


import com.mielec.users.model.UserRole;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "project")
@Table(name = "project")
public class Project {

    private int id;
    private String name;


    public Project() {
    }

    public Project(int id,String name) {
        this.id=id;
        this.name=name;
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

    @Column(name = "name",
            nullable = false, length = 30)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name=name;
    }

}