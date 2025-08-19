package pet.innoQuiz.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pet.innoQuiz.exception.QuestionNotExistException;
import pet.innoQuiz.model.dto.*;
import pet.innoQuiz.model.entity.*;
import pet.innoQuiz.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizResultRepository quizResultRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public QuizResponse createQuiz(
            String title,
            String description,
            Long authorId,
            List<QuestionRequest> questionRequests
    ) {
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);

        Profile author = profileRepository.findById(authorId).get();
        quiz.setAuthor(author);

        List<Question> questions = questionRequests.stream()
                .map(qRequest -> {
                    Question question = new Question();
                    question.setText(qRequest.getText());
                    question.setQuiz(quiz);

                    List<Answer> answers = qRequest.getAnswers().stream()
                            .map(aRequest -> {
                                Answer answer = new Answer();
                                answer.setText(aRequest.getText());
                                answer.setCorrect(aRequest.isCorrect());
                                answer.setQuestion(question);
                                return answer;
                            })
                            .toList();

                    question.setAnswers(answers);
                    return question;
                })
                .toList();

        quiz.setQuestions(questions);

        Quiz savedQuiz = quizRepository.save(quiz);

        return mapToQuizResponse(savedQuiz);
    }

    private QuizResponse mapToQuizResponse(Quiz quiz) {
        QuizResponse response = new QuizResponse();
        response.setId(quiz.getId());
        response.setTitle(quiz.getTitle());
        response.setDescription(quiz.getDescription());

        response.setQuestions(quiz.getQuestions().stream()
                .map(question -> {
                    QuestionResponse qResponse = new QuestionResponse();
                    qResponse.setId(question.getId());
                    qResponse.setText(question.getText());

                    qResponse.setAnswers(question.getAnswers().stream()
                            .map(answer -> {
                                AnswerResponse aResponse = new AnswerResponse();
                                aResponse.setId(answer.getId());
                                aResponse.setText(answer.getText());
                                aResponse.setCorrect(answer.isCorrect());
                                return aResponse;
                            })
                            .toList());

                    return qResponse;
                })
                .toList());

        return response;
    }

    @Transactional
    public List<GetQuizResponse> getQuiz(int limit){
        List<Quiz> quiz = quizRepository.findLimitedQuizzes(limit);

        return quiz.stream()
                .map(q -> new GetQuizResponse(
                        q.getTitle(),
                        q.getDescription(),
                        q.getAuthor().getUser().getUsername()
                )).toList();
    }

    @Transactional
    public Page<Quiz> getPublicQuizzes(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return quizRepository.findByIsPublicTrue(pageable);
    }

    @Transactional
    public void deleteQuestion(Long id){
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotExistException("Question with id = " + id + " does not exist"));

        questionRepository.delete(question);
    }

    public QuizSessionDto startQuizSession(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        return new QuizSessionDto(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getQuestions().stream()
                        .map(this::convertToQuestionDto)
                        .collect(Collectors.toList())
        );
    }

    public QuizResultDto submitQuiz(QuizSubmissionDto submission) {
        Quiz quiz = quizRepository.findById(submission.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        Profile profile = profileRepository.findById(submission.getProfileId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        int score = calculateScore(submission, quiz);

        QuizResult result = new QuizResult();
        result.setUser(profile);
        result.setQuiz(quiz);
        result.setScore(score);
        result.setCompletedAt(LocalDateTime.now());

        quizResultRepository.save(result);

        return new QuizResultDto(
                quiz.getId(),
                quiz.getTitle(),
                score,
                quiz.getQuestions().size(),
                LocalDateTime.now()
        );
    }

    private int calculateScore(QuizSubmissionDto submission, Quiz quiz) {
        return (int) submission.getAnswers().stream()
                .filter(userAnswer -> isAnswerCorrect(userAnswer, quiz))
                .count();
    }

    private boolean isAnswerCorrect(UserAnswerDto userAnswer, Quiz quiz) {
        return quiz.getQuestions().stream()
                .filter(q -> q.getId().equals(userAnswer.getQuestionId()))
                .flatMap(q -> q.getAnswers().stream())
                .anyMatch(a -> a.getId().equals(userAnswer.getAnswerId()) && a.isCorrect());
    }

    private QuestionDto convertToQuestionDto(Question question) {
        return new QuestionDto(
                question.getId(),
                question.getText(),
                question.getAnswers().stream()
                        .map(this::convertToAnswerDto)
                        .collect(Collectors.toList())
        );
    }

    private AnswerDto convertToAnswerDto(Answer answer) {
        return new AnswerDto(answer.getId(), answer.getText());
    }

    /*@Transactional
    public QuizResponse updateQuiz(Long id, ){
        return null;
    }*/
}
