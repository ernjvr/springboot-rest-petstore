/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.petstore.user.dao;

import com.petstore.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author admin
 */
public interface UserRepository extends JpaRepository<User, Long>{
        User findByUsername(String username);
}
