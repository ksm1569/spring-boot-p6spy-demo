package com.smsoft.springbootp6spydemo.repository;

import com.smsoft.springbootp6spydemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
} 