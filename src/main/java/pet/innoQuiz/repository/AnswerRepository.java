package pet.innoQuiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet.innoQuiz.model.entity.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
