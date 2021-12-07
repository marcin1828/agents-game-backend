package pl.miernik.codenamesbackend.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tile {
    private int imageNumber;
    private Color color;
}
