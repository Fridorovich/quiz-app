package pet.innoQuiz.model.dto;

import lombok.Data;
import pet.innoQuiz.model.entity.Answer;

import java.util.List;

@Data
public class QuestionResponse {
    private Long id;
    private String text;
    private List<AnswerResponse> answers;
}
