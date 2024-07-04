package com.project.liar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.project.liar.config.ChatHandler;
import com.project.liar.entity.Acticle;
import com.project.liar.entity.GameRoom;
import com.project.liar.entity.Player;
import com.project.liar.repository.ActicleRepository;
import com.project.liar.repository.GameRoomRepository;
import com.project.liar.repository.PlayerRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class LiarGameController {

    @Autowired
    private GameRoomRepository gameRoomRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ChatHandler chatHandler;

    @Autowired
    private ActicleRepository acticleRepository;

    @GetMapping("/liar")
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

        // Acticle의 개수를 가져와서 해당 범위 내에서 랜덤 Acticle 선택 및 kind 저장
        List<Acticle> acticles = acticleRepository.findAll();
        Random random = new Random();
        Acticle randomActicle = acticles.get(random.nextInt(acticles.size()));
        gameRoom.setRandomValue(randomActicle.getKind());  // Acticle의 kind 값을 randomValue로 설정

        gameRoomRepository.save(gameRoom);

        Player player = new Player();
        player.setName(gameRoom.getCreator());
        player.setGameRoom(gameRoom);
        playerRepository.save(player);

        model.addAttribute("gameRoom", gameRoom);
        model.addAttribute("isCreator", true);

        return "redirect:/lobby/" + gameRoom.getId() + "?playerName=" + gameRoom.getCreator();
    }

    @PostMapping("/joinRoom")
    public String joinRoom(@RequestParam Long roomId, @RequestParam String playerName, Model model) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        Player player = new Player();
        player.setName(playerName);
        player.setGameRoom(gameRoom);
        playerRepository.save(player);
        model.addAttribute("gameRoom", gameRoom);
        model.addAttribute("isCreator", gameRoom.getCreator().equals(playerName));
        return "redirect:/lobby/" + roomId + "?playerName=" + playerName;
    }

    @GetMapping("/lobby/{roomId}")
    public String lobby(@PathVariable Long roomId, @RequestParam String playerName, Model model) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        model.addAttribute("gameRoom", gameRoom);
        model.addAttribute("isCreator", gameRoom.getCreator().equals(playerName));
        model.addAttribute("playerName", playerName);
        return "lobby";
    }

    @PostMapping("/deleteRoom")
    public String deleteRoom(@RequestParam Long roomId, @RequestParam String creator) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        if (!gameRoom.getCreator().equals(creator)) {
            throw new RuntimeException("You are not the creator of this room");
        }
        gameRoomRepository.deleteById(roomId);
        return "redirect:/";
    }

    @GetMapping("/start/{roomId}")
    public String gameStarted(@PathVariable Long roomId, @RequestParam String playerName, Model model) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));

        // 모든 플레이어 가져오기
        List<Player> players = gameRoom.getPlayers();
        if (players.isEmpty()) {
            throw new RuntimeException("No players in the room");
        }

        // 랜덤으로 한 명의 플레이어를 라이어로 설정
        Random random = new Random();
        Player liar = players.get(random.nextInt(players.size()));
        liar.setIsLiar(true);
        playerRepository.save(liar);  // 라이어 정보 저장

        // 라이어의 이름을 GameRoom에 저장
        gameRoom.setLiarName(liar.getName());
        gameRoomRepository.save(gameRoom);

        model.addAttribute("gameRoom", gameRoom);
        model.addAttribute("randomValue", gameRoom.getRandomValue());

        // 현재 플레이어가 라이어인지 확인
        boolean isLiar = liar.getName().equals(playerName);
        model.addAttribute("isLiar", isLiar);
        model.addAttribute("playerName", playerName);

        return "gameStarted";
    }

    @PostMapping("/start/{roomId}")
    public String gameStartedPost(@PathVariable Long roomId, Model model) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        model.addAttribute("gameRoom", gameRoom);
        model.addAttribute("randomValue", gameRoom.getRandomValue());
        return "gameStarted";
    }

    @GetMapping("/result/{roomId}")
    public String result(@PathVariable Long roomId, @RequestParam String playerName, Model model) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        model.addAttribute("gameRoom", gameRoom);
        model.addAttribute("playerName", playerName);
        return "result";
    }

    @GetMapping("/vote/{playerName}")
    @ResponseBody
    public String voteLiar(@RequestParam Long roomId, @PathVariable String playerName) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        Player player = playerRepository.findByNameAndGameRoom(playerName, gameRoom)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        if (player.getIsLiar()) {
            return "라이어가 맞습니다.";
        } else {
            return "라이어가 아닙니다.";
        }
    }

    @DeleteMapping("/exit/{roomId}")
    @ResponseBody
    public String exitGame(@PathVariable Long roomId, @RequestParam String playerName) {
        GameRoom gameRoom = gameRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        Player player = playerRepository.findByNameAndGameRoom(playerName, gameRoom)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // 플레이어 삭제
        playerRepository.delete(player);

        // 게임룸 삭제
        gameRoomRepository.delete(gameRoom);

        return "삭제 완료";
    }
}
