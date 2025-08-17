package pet.innoQuiz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.innoQuiz.model.dto.GetQuizResponse;
import pet.innoQuiz.model.dto.QuizRequest;
import pet.innoQuiz.model.dto.QuizResponse;
import pet.innoQuiz.model.entity.Quiz;
import pet.innoQuiz.service.QuizService;

import java.util.List;

@RestController
@RequestMapping("api/public")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/get_quiz")
    public ResponseEntity<List<GetQuizResponse>> getQuiz(@RequestParam int limit){
        List<GetQuizResponse> response = quizService.getQuiz(limit);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/quiz")
    public ResponseEntity<QuizResponse> createQuiz(@RequestBody QuizRequest request){
        System.out.println("Request authorId: " + request.getAuthorId() + " (type: " + request.getAuthorId().getClass() + ")");

        QuizResponse response = quizService.createQuiz(
                request.getTitle(),
                request.getDescription(),
                request.getAuthorId(),
                request.getQuestionRequests()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/publicQuiz")
    public ResponseEntity<Page<Quiz>> getPublicQuizzes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int limit) {

        Page<Quiz> quizzes = quizService.getPublicQuizzes(page, limit);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/check")
    public boolean checkAuth(){
        return true;
    }
}
