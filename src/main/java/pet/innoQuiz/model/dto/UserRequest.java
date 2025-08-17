package pet.innoQuiz.model.dto;

public record UserRequest(
        String username,
        String email,
        String password
) {
}
