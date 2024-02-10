package antoniogiovanni.marchese.CapstoneBackend.payloads;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;

public record UserLoginResponseDTO(String token, Role role) {
}
