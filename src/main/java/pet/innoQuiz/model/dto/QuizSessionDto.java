package pet.innoQuiz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class QuizSessionDto {
    private Long quizId;
    private String quizTitle;
    private List<QuestionDto> questions;
}
