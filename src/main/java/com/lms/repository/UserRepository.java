package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.entity.User;

public interface UserRepository extends JpaRepository<User,Integer> {


}