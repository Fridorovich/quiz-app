package pet.innoQuiz.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuizResponse {
    private Long id;
    private String title;
    private String description;
    private List<QuestionResponse> questions;
}
