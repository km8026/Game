package com.project.liar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.liar.entity.GameRoom;

public interface GameRoomRepository extends JpaRepository <GameRoom, Long>{
  
}
