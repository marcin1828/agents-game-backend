package pl.miernik.codenamesbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.miernik.codenamesbackend.data.GameStatus;
import pl.miernik.codenamesbackend.service.GameService;


@RestController
@CrossOrigin
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/createGame")
    public GameStatus createGame() {
        return gameService.createGame();
    }

}
