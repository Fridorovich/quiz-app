package pet.innoQuiz.exception;

public class QuestionNotExistException extends RuntimeException{
    public QuestionNotExistException(String message) {
        super(message);
    }
}
