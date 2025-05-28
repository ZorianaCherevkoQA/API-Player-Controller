package api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuperUserRole {
    SUPERVISOR("supervisor"),
    ADMIN("admin");

    private final String role;
}
