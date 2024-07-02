package com.project.liar.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class GameRoom {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private boolean isActive;
  private String creator;

     @OneToMany(mappedBy = "gameRoom")
    private List<Player> players;
  
    public int getPlayerCount() {
      return players == null ? 0 : players.size();
  }
}
