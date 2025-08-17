package pet.innoQuiz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet.innoQuiz.model.dto.*;
import pet.innoQuiz.service.AuthService;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /*@PostMapping("/register")
    public ResponseEntity<JwtDto> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestBody RefreshTokenDto request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }*/

    @PostMapping("/login")
    public ResponseEntity<JwtDto> singIn(@RequestBody UserCredentialsDto userCredentialsDto) {
        try {
            JwtDto jwtAuthenticationDto = authService.singIn(userCredentialsDto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/refresh")
    public JwtDto refresh(@RequestBody RefreshTokenDto refreshTokenDto) throws Exception {
        return authService.refreshToken(refreshTokenDto); 
    }

    @PostMapping("/registration")
    public String createUser(@RequestBody UserRequest userDto) {
        return authService.addUser(userDto.username(),userDto.email(), userDto.password());
    }

    @GetMapping
    public boolean validateToken(String token){
        return authService.isTokenValid(token);
    }
}