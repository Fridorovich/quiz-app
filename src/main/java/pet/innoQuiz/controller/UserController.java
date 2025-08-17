package pet.innoQuiz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.innoQuiz.model.dto.UserRequest;
import pet.innoQuiz.model.dto.UserResponse;
import pet.innoQuiz.model.entity.User;
import pet.innoQuiz.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request){
        UserResponse savedUser = userService.createUser(request.username(), request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@RequestParam Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = userService.getUsers();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@RequestParam Long id){
        return ResponseEntity.ok(userService.getMe(id));
    }
}
