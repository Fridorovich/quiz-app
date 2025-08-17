package pet.innoQuiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pet.innoQuiz.model.entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query(value = "SELECT * FROM quizzes LIMIT :limit", nativeQuery = true)
    List<Quiz> findLimitedQuizzes(@Param("limit") int limit);

    Page<Quiz> findByIsPublicTrue(Pageable pageable);

    @Query("SELECT q FROM Quiz q LEFT JOIN FETCH q.questions WHERE q.id = :id")
    Quiz findByIdWithQuestions(Long id);
}
