package pl.miernik.codenamesbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerInfo {
    private String id;
    private String name;
    private String team;
    private String role;
}