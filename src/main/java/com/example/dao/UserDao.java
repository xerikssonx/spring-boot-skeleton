package com.example.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.entity.User;

@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserDao extends CrudRepository<User, Long> {

	User findByUsername(String username);
	
}
