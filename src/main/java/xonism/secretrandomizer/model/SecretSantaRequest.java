package xonism.secretrandomizer.model;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record SecretSantaRequest(
        List<Participant> participants
) {
    public record Participant(@NotBlank(message = "Name is required")
                              String name,
                              @NotBlank(message = "Email address is required")
                              String emailAddress
    ) {
    }
}
