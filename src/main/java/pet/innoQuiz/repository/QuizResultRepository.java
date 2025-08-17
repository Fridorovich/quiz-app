package pet.innoQuiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pet.innoQuiz.model.entity.QuizResult;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
}
