package pet.innoQuiz.model.dto;

public record GetQuizResponse(
        String title,
        String description,
        String authorUsername
) {
}
