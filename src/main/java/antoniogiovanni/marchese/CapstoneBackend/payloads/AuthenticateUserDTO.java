package antoniogiovanni.marchese.CapstoneBackend.payloads;

import antoniogiovanni.marchese.CapstoneBackend.model.User;

public record AuthenticateUserDTO(String accessToken, User user) {
}
