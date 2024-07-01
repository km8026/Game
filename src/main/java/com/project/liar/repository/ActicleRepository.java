package com.project.liar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.liar.entity.Acticle;

public interface ActicleRepository extends JpaRepository <Acticle, Long> {
  
}
