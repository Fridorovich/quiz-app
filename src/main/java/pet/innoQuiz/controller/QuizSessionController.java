package pet.innoQuiz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pet.innoQuiz.model.dto.*;
import pet.innoQuiz.model.entity.Profile;
import pet.innoQuiz.service.QuizService;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizSessionController {

    private final QuizService quizService;

    @GetMapping("/{quizId}/start")
    public QuizSessionDto startQuiz(@PathVariable Long quizId) {
        return quizService.startQuizSession(quizId);
    }

    @PostMapping("/submit")
    public QuizResultDto submitQuiz(
            @RequestBody QuizSubmissionDto submission
    ) {
        return quizService.submitQuiz(submission);
    }
}