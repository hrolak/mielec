package com.mielec.project.dao;


import com.mielec.project.model.Project;

public interface ProjectDao {
    Project findProjectById(int id);
}
