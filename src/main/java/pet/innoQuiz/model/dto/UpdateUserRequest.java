package pet.innoQuiz.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {
    private String email;
    private String username;
    private String password;
}
