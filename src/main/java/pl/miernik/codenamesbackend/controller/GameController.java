package pl.miernik.codenamesbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.miernik.codenamesbackend.data.Color;
import pl.miernik.codenamesbackend.data.GameStatus;
import pl.miernik.codenamesbackend.dto.PlayerInfo;
import pl.miernik.codenamesbackend.dto.Players;
import pl.miernik.codenamesbackend.service.GameService;

import java.util.Collections;
import java.util.Map;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    @GetMapping("/create")
    public Map<String, String> createGame() {
        return Collections.singletonMap("gameId", gameService.createGame());
    }

    @GetMapping("/{gameId}/validate")
    public Map<String, Boolean> validateGameId(@PathVariable String gameId) {
        return Collections.singletonMap("valid", gameService.validateGameId(gameId));
    }

    @PostMapping("/{gameId}/join")
    public Map<String, String> joinToGame(@PathVariable String gameId, @RequestBody Map<String,String> body) {
        return Collections.singletonMap("userId", gameService.joinToGame(
                gameId,
                body.get("team"),
                body.get("role"),
                body.get("userName")));
    }

    @GetMapping("/{gameId}/status/players")
    public Players getPlayers(@PathVariable String gameId) {
        return gameService.getPlayers(gameId);
    }

    @GetMapping("/{gameId}/status/player/{playerId}")
    public PlayerInfo getPlayerInfo(@PathVariable String gameId, @PathVariable String playerId) {
        return gameService.getPlayerInfo(gameId, playerId);
    }

    @GetMapping("/{gameId}/status/private")
    public GameStatus getPrivateGameStatus(@PathVariable String gameId) {
        return gameService.getPrivateGameStatus(gameId);
    }

    @GetMapping("/{gameId}/status/public")
    public GameStatus getPublicGameStatus(@PathVariable String gameId) {
        return gameService.getPublicGameStatus(gameId);
    }

    @GetMapping("/{gameId}/tile/color")
    public Color getGameStatus(@PathVariable String gameId, @RequestParam int imageNumber) {
        return gameService.getTileColor(gameId, imageNumber);
    }
}
