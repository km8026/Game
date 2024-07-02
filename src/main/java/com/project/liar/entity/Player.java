package com.project.liar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Player {
  @Id @GeneratedValue( strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private Boolean isLiar;

  @ManyToOne
    @JoinColumn(name = "gameRomm")
    private GameRoom gameRoom;
  
}
