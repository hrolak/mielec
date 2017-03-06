package com.mielec.department.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

import com.mielec.users.model.UserRole;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "department")
@Table(name = "department")
public class Department {

    private String id;
    private String name;

    public Department() {
    }

    public Department(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @Column(name = "id", unique = true,
            nullable = false, length = 3)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
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