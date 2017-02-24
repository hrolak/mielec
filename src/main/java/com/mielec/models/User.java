package com.mielec.models;

import javax.persistence.Entity;

@Entity
@NamedQuery(name = "User.findByEmailAddress",
        query = "select u from User u where u.emailAddress = ?1")
public class User {

}