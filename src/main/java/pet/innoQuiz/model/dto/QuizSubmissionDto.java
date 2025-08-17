package pet.innoQuiz.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class QuizSubmissionDto {
    private Long profileId;
    private Long quizId;
    private List<UserAnswerDto> answers;
}