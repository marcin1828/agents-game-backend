package pl.miernik.codenamesbackend.data;

import lombok.Data;
import javax.persistence.*;


@Entity
@Data
public class Tile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isOpen;
    private int imageNumber;
    private Color color;

    public Tile(boolean isOpen, int imageNumber, Color color) {
        this.isOpen = isOpen;
        this.imageNumber = imageNumber;
        this.color = color;
    }

    public Tile() {
    }
}
