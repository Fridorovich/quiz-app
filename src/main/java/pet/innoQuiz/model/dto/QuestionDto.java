package pet.innoQuiz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuestionDto {
    private Long id;
    private String text;
    private List<AnswerDto> answers;
}

