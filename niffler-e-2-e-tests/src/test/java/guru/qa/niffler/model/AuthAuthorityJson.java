package guru.qa.niffler.model;

import java.util.UUID;

public record AuthAuthorityJson(
        UUID id,
        int userId,
        String authority) {
}
