package pet.innoQuiz.model;

import lombok.Data;

@Data
class UserAnswerDto {
    private Long questionId;
    private Long answerId;
}