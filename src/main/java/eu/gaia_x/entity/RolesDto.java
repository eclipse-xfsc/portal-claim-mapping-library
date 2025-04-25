package eu.gaia_x.entity;

import java.util.List;
import lombok.Data;

@Data
public class RolesDto {
    private String context;
    private List<String> roles;
}
