package pet.innoQuiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.innoQuiz.exception.EmailAlreadyExistsException;
import pet.innoQuiz.exception.UserAlreadyExistsException;
import pet.innoQuiz.model.dto.JwtDto;
import pet.innoQuiz.model.dto.RefreshTokenDto;
import pet.innoQuiz.model.dto.UserCredentialsDto;
import pet.innoQuiz.model.dto.UserResponse;
import pet.innoQuiz.model.entity.Profile;
import pet.innoQuiz.model.entity.User;
import pet.innoQuiz.repository.UserRepository;
import pet.innoQuiz.utils.JwtService;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public boolean isTokenValid(String token){
        return jwtService.validateJwtToken(token);
    }
    public JwtDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        User user = findByCredentials(userCredentialsDto);
        return jwtService.generateAuthToken(user);
    }

    public JwtDto refreshToken(RefreshTokenDto refreshTokenDto) throws Exception {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
            User user = findByEmail(jwtService.getEmailFromToken(refreshToken));
            return jwtService.refreshBaseToken(user, refreshToken);
        }
        throw new  AuthenticationException("Invalid refresh token");
    }

    public String addUser(String username, String email, String password){
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistsException("Username '" + username + "' already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email '" + email + "' is already registered");
        }

        String encodedPassword = passwordEncoder.encode(password);

        Profile profile = new Profile();
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole(User.Role.USER);
        user.setProfile(profile);
        profile.setUser(user);

        User savedUser = userRepository.save(user);
        return "User added";
    }

    private User findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        Optional<User> optionalUser = userRepository.findByEmail(userCredentialsDto.getEmail());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            if (passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())){
                return user;
            }
        }
        throw new AuthenticationException("Email or password is not correct");
    }

    private User findByEmail(String email) throws Exception {
        return userRepository.findByEmail(email).orElseThrow(()->
                new Exception(String.format("User with email %s not found", email)));
    }
}