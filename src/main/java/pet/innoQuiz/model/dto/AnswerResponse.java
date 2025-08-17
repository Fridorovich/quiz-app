package pet.innoQuiz.model.dto;

import lombok.Data;

@Data
public class AnswerResponse {
    private Long id;
    private String text;
    private boolean isCorrect;
}