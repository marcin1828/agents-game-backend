package pl.miernik.codenamesbackend.service;

import org.springframework.stereotype.Service;
import pl.miernik.codenamesbackend.data.Color;
import pl.miernik.codenamesbackend.data.GameStatus;
import pl.miernik.codenamesbackend.data.Tile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    private static final int IMAGES_COUNT = 30;

    public GameStatus createGame() {
        String token = generateToken();
        List<Tile> tiles = generateRandomBlankTiles();

        GameStatus gameStatus = new GameStatus(token, tiles);
        setRandomColorTiles(gameStatus.getTiles());


        return gameStatus;
    }

    private List<Tile> generateRandomBlankTiles() {
        List<Integer> imagesNumbers = new ArrayList<Integer>(IMAGES_COUNT);

        for(int i = 1; i <= IMAGES_COUNT; i++) {
            imagesNumbers.add(i);
        }

        //shuffle list
        Collections.shuffle(imagesNumbers);

        // return first 25 tiles
        return imagesNumbers.stream().limit(25).map(x -> new Tile(x, null)).collect(Collectors.toList());
    }

    private String generateToken() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private void setRandomColorTiles(List<Tile> tiles) {

        Random random = new Random();

        // black card draw
        int blackIndex = random.nextInt(25);
        tiles.get(blackIndex).setColor(Color.BLACK);

        // red card draw
        for(int i = 9; i > 0; i--) {
            int redIndex = random.nextInt(25);
            if(tiles.get(redIndex).getColor() == null) {
                tiles.get(redIndex).setColor(Color.RED);
            }
            else {
                i++;
            }
        }

        // blue card draw
        for(int i = 8; i > 0; i--) {
            int blueIndex = random.nextInt(25);
            if(tiles.get(blueIndex).getColor() == null) {
                tiles.get(blueIndex).setColor(Color.BLUE);
            }
            else {
                i++;
            }
        }

        //other cards in gray color
        for(Tile tile : tiles) {
            if(tile.getColor() == null) {
                tile.setColor(Color.GRAY);
            }
        }

    }
}
