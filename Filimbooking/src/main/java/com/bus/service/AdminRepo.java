package com.bus.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bus.beans.Admin;

public interface AdminRepo extends JpaRepository<Admin, Long>{
		public Admin findByUsernameAndPassword(String username, String password);
}
