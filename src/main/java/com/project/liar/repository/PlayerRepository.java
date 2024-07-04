package com.project.liar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.liar.entity.GameRoom;
import com.project.liar.entity.Player;


public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByNameAndGameRoom(String name, GameRoom gameRoom);
}

