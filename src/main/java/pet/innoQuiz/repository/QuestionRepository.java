package pet.innoQuiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.innoQuiz.model.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
