package pet.innoQuiz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerDto {
    private Long id;
    private String text;
}
