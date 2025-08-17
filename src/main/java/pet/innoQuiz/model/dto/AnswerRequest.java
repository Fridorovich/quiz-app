package pet.innoQuiz.model.dto;

import lombok.Data;

@Data
public class AnswerRequest {
    private String text;
    private boolean isCorrect;
}
