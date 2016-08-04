package com.example.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.entity.Role;

@RepositoryRestResource(collectionResourceRel = "role", path = "role")
public interface RoleDao extends CrudRepository<Role, Long>{

}
