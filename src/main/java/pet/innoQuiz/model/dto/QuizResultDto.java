package pet.innoQuiz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class QuizResultDto {
    private Long quizId;
    private String quizTitle;
    private int score;
    private int totalQuestions;
    private LocalDateTime completedAt;
}