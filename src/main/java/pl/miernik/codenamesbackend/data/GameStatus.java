package pl.miernik.codenamesbackend.data;

import lombok.Data;
import javax.persistence.*;
import java.util.List;


@Data
@Entity
public class GameStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Tile> tiles;

    public GameStatus(String token, List<Tile> tiles) {
        this.token = token;
        this.tiles = tiles;
    }

    public GameStatus() {
    }
}
