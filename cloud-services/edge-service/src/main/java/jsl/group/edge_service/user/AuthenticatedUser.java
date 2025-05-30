package jsl.group.edge_service.user;

import java.util.List;

public record AuthenticatedUser(
        String username, String firstname, String lastname, List<String> roles
) {
}
