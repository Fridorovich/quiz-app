package pet.innoQuiz.model.dto;

import jakarta.persistence.*;
import lombok.Builder;
import pet.innoQuiz.model.entity.Profile;
import pet.innoQuiz.model.entity.User;

@Builder
public record UserResponse (
        Long id,
        String username,
        String email,
        User.Role role,
        Long profile_id
){}
