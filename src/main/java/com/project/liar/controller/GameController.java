package com.project.liar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.project.liar.entity.Acticle;
import com.project.liar.entity.GameRoom;
import com.project.liar.entity.Player;
import com.project.liar.repository.ActicleRepository;
import com.project.liar.repository.GameRoomRepository;
import com.project.liar.repository.PlayerRepository;

import java.util.List;
import java.util.Random;

@Controller
public class GameController {

    @Autowired
    private ActicleRepository acticleRepository;

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<GameRoom> gameRooms = gameRoomRepository.findAll();
        model.addAttribute("gameRooms", gameRooms);
        return "list";
    }

    @GetMapping("/createRoom")
    public String createRoomForm(Model model) {
        model.addAttribute("gameRoom", new GameRoom());
        return "createRoom";
    }

    @PostMapping("/createRoom")
    public String createRoom(@ModelAttribute GameRoom gameRoom, Model model) {
        gameRoom.setActive(true);
        gameRoomRepository.save(gameRoom);
        Player player = new Player();
        player.setName(gameRoom.getCreator());
        player.setGameRoom(gameRoom);
        playerRepository.save(player);
        model.addAttribute("gameRoom", gameRoom);
        return "redirect:/lobby/" + gameRoom.getId();
    }

    @PostMapping("/joinRoom")
    public String joinRoom(@RequestParam Long roomId, @RequestParam String playerName, Model model) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("방을 못찾았습니다"));
        Player player = new Player();
        player.setName(playerName);
        player.setGameRoom(gameRoom);
        playerRepository.save(player);
        model.addAttribute("gameRoom", gameRoom);
        return "redirect:/lobby/" + roomId;
    }

    @GetMapping("/lobby/{roomId}")
    public String lobby(@PathVariable Long roomId, Model model) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("방을 못찾았습니다"));
        model.addAttribute("gameRoom", gameRoom);
        return "lobby";
    }

    @PostMapping("/deleteRoom")
    public String deleteRoom(@RequestParam Long roomId, @RequestParam String creator) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("방을 못찾았습니다"));
        if (!gameRoom.getCreator().equals(creator)) {
            throw new RuntimeException("당신이 만든 방이 아닙니다");
        }
        gameRoomRepository.deleteById(roomId);
        return "redirect:/";
    }

    @PostMapping("/start/{roomId}")
    public String startGame(@PathVariable Long roomId, Model model) {
        // 게임 시작 로직을 여기에 추가
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        model.addAttribute("gameRoom", gameRoom);
        List<Acticle> allActicles = acticleRepository.findAll();
        Random random = new Random();
        Acticle randomActicle = allActicles.get(random.nextInt(allActicles.size()));
        model.addAttribute("randomActicle", randomActicle);
        return "gameStarted"; // 게임 시작 후 이동할 페이지
    }
    
       
}
