package xonism.secretrandomizer.model;

public record Email(
        String to,
        String subject,
        String message
) {
}
