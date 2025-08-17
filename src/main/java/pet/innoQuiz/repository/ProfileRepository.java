package pet.innoQuiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pet.innoQuiz.model.entity.Profile;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    public Optional<Profile> findById(Integer id);
}
