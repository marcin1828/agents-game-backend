package pl.miernik.codenamesbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.miernik.codenamesbackend.data.Color;
import pl.miernik.codenamesbackend.data.GameStatus;
import pl.miernik.codenamesbackend.data.Tile;
import pl.miernik.codenamesbackend.dto.PlayerInfo;
import pl.miernik.codenamesbackend.dto.Players;
import pl.miernik.codenamesbackend.repository.GameStatusRepo;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameStatusRepo gameStatusRepo;
    @Value("${image.count}")
    private int IMAGE_COUNT;


    public String createGame() {
        String token = generateToken();
        List<Tile> tiles = generateRandomBlankTiles();
        GameStatus gameStatus = new GameStatus(token, tiles);
        setRandomColorTiles(gameStatus.getTiles());
        gameStatusRepo.save(gameStatus);
        return token;
    }

    private List<Tile> generateRandomBlankTiles() {
        List<Integer> imageNumbers = new ArrayList<>(IMAGE_COUNT);

        for(int i = 1; i <= IMAGE_COUNT; i++) {
            imageNumbers.add(i);
        }

        //shuffle list
        Collections.shuffle(imageNumbers);

        // return 25 tiles from the beginning
        return imageNumbers.stream()
                .limit(25)
                .map(imgNo -> new Tile(false, imgNo, null))
                .collect(Collectors.toList());
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

    public GameStatus getPrivateGameStatus(String token) {
        return gameStatusRepo.findAllByToken(token);
    }

    public GameStatus getPublicGameStatus(String token) {
        GameStatus gameStatus = gameStatusRepo.findAllByToken(token);
        List<Tile> publicTileList = gameStatus.getTiles().stream()
                .map(tile -> {
                    if(!tile.isOpen()) tile.setColor(null);
                    return tile;
                })
                .collect(Collectors.toList());

        gameStatus.setTiles(publicTileList);
        return gameStatus;
    }

    @Transactional
    public Color getTileColor(String token, int imageNumber) {

        GameStatus gameStatus = gameStatusRepo.findAllByToken(token);

        List<Tile> tiles = gameStatus.getTiles()
                .stream()
                .map(tile -> {
                    if(tile.getImageNumber() == imageNumber) {
                        tile.setOpen(true);
                    }
                    return tile;
                })
                .collect(Collectors.toList());

        gameStatus.setTiles(tiles);
        gameStatusRepo.save(gameStatus);

        return tiles.stream()
                .filter(tile -> tile.getImageNumber() == imageNumber)
                .findFirst()
                .map(Tile::getColor)
                .orElse(null);
    }

    public boolean validateGameId(String id) {
        return gameStatusRepo.findAllByToken(id) != null;
    }

    @Transactional
    public String joinToGame(String gameId, String team, String role, String userName) {
        String userId = this.generateToken();
        GameStatus gameStatus = gameStatusRepo.findAllByToken(gameId);
        if (team.equals("blue")) {
            if (role.equals("leader")) {
                if(gameStatus.getBlueLeaderId().equals("") && gameStatus.getBlueLeaderName().equals("")) {
                    gameStatus.setBlueLeaderId(userId);
                    gameStatus.setBlueLeaderName(userName);
                    gameStatusRepo.save(gameStatus);
                    return userId;
                }
            } else {
                if(gameStatus.getBlueAgentId().equals("") && gameStatus.getBlueAgentName().equals("")) {
                    gameStatus.setBlueAgentId(userId);
                    gameStatus.setBlueAgentName(userName);
                    gameStatusRepo.save(gameStatus);
                    return userId;
                }
            }
        } else {
            if (role.equals("leader")) {
                if(gameStatus.getRedLeaderId().equals("") && gameStatus.getRedLeaderName().equals("")) {
                    gameStatus.setRedLeaderId(userId);
                    gameStatus.setRedLeaderName(userName);
                    gameStatusRepo.save(gameStatus);
                    return userId;
                }
            } else {
                if(gameStatus.getRedAgentId().equals("") && gameStatus.getRedAgentName().equals("")) {
                    gameStatus.setRedAgentId(userId);
                    gameStatus.setRedAgentName(userName);
                    gameStatusRepo.save(gameStatus);
                    return userId;
                }
            }
        }
        return null;
    }

    public Players getPlayers(String gameId) {
        GameStatus status = gameStatusRepo.findAllByToken(gameId);
        return new Players(
                status.getBlueLeaderId(),
                status.getBlueLeaderName(),
                status.getBlueAgentId(),
                status.getBlueAgentName(),
                status.getRedLeaderId(),
                status.getRedLeaderName(),
                status.getRedAgentId(),
                status.getRedAgentName());
    }

    public PlayerInfo getPlayerInfo(String gameId, String playerId) {
        GameStatus status = gameStatusRepo.findAllByToken(gameId);

        if (playerId.equals(status.getBlueAgentId())) {
            return new PlayerInfo(playerId, status.getBlueAgentName(), "blue", "agent");
        } else if (playerId.equals(status.getBlueLeaderId())) {
            return new PlayerInfo(playerId, status.getBlueLeaderName(), "blue", "leader");
        } else if (playerId.equals(status.getRedAgentId())) {
            return new PlayerInfo(playerId, status.getRedAgentName(), "red", "agent");
        } else if (playerId.equals(status.getRedLeaderId())) {
            return new PlayerInfo(playerId, status.getRedLeaderName(), "red", "leader");
        }
        return null;
    }
}
