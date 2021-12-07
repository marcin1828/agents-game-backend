package pl.miernik.codenamesbackend.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GameStatus {
    private String token;
    private List<Tile> tiles;
}
