package com.project.liar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.liar.entity.Player;

public interface PlayerRepository extends JpaRepository <Player,Long>{

  public List<Player> findAllByGameRoomId(Long roomId);
  
}
