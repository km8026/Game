package com.project.liar.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class GameRoom {
  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private boolean isActive;
  private String creator;
  private String randomValue;  // 랜덤 값을 저장할 필드 추가
  private String liarName;

  @ManyToOne  // Acticle과의 관계 설정
  private Acticle acticle;

  @OneToMany(mappedBy = "gameRoom")
  private List<Player> players;

  public int getPlayerCount() {
    return players == null ? 0 : players.size();
  }
}
