package com.project.liar.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.liar.entity.Player;
import com.project.liar.repository.GameRoomRepository;
import com.project.liar.repository.PlayerRepository;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRoomRepository gmaeRoomRepository;

    @PostMapping
    public Player joinGameRoom(@RequestBody Player player) {
        return playerRepository.save(player);
    }

    @GetMapping("/{roomId}")
    public List<Player> getPlayersInGameRoom(@PathVariable Long roomId) {
        return playerRepository.findAllByGameRoomId(roomId);
    }
}
