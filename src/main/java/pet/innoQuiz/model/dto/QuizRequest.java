package pet.innoQuiz.model.dto;

import lombok.Data;
import pet.innoQuiz.model.entity.Profile;

import java.util.List;

@Data
public class QuizRequest{
    String title;
    String description;
    Long authorId;
    List<QuestionRequest> questionRequests;
}
