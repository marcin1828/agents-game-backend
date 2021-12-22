package pl.miernik.codenamesbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.miernik.codenamesbackend.data.Color;
import pl.miernik.codenamesbackend.data.GameStatus;
import pl.miernik.codenamesbackend.service.GameService;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    @GetMapping("/create")
    public String createGame() {
        return gameService.createGame();
    }

    @GetMapping("/{token}/status/private")
    public GameStatus getPrivateGameStatus(@PathVariable String token) {
        return gameService.getPrivateGameStatus(token);
    }

    @GetMapping("/{token}/status/public")
    public GameStatus getPublicGameStatus(@PathVariable String token) {
        return gameService.getPublicGameStatus(token);
    }

    @GetMapping("/{token}/tile/color")
    public Color getGameStatus(@PathVariable String token, @RequestParam int imageNumber) {
        return gameService.getTileColor(token, imageNumber);
    }
}
