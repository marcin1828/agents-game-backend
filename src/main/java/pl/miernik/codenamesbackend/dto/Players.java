package pl.miernik.codenamesbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Players {
    private String blueLeaderId;
    private String blueLeaderName;
    private String blueAgentId;
    private String blueAgentName;
    private String redLeaderId;
    private String redLeaderName;
    private String redAgentId;
    private String redAgentName;
}
