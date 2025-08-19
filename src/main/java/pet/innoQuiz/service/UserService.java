package pet.innoQuiz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet.innoQuiz.exception.EmailAlreadyExistsException;
import pet.innoQuiz.exception.UserAlreadyExistsException;
import pet.innoQuiz.exception.UserNotExistException;
import pet.innoQuiz.model.dto.UpdateUserRequest;
import pet.innoQuiz.model.dto.UserResponse;
import pet.innoQuiz.model.entity.Profile;
import pet.innoQuiz.model.entity.User;
import pet.innoQuiz.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(String username, String email, String password){
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

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getProfile().getId()
        );
    }

    @Transactional
    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistException("User with id = " + id + " does not exist"));

        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserResponse getMe(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found"));

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getProfile().getId()
        );
    }

    @Transactional
    public UserResponse updateMe(Long id, UpdateUserRequest updateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotExistException("User with id " + id + " not found"));

        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateRequest.getEmail())) {
                throw new EmailAlreadyExistsException("Email " + updateRequest.getEmail() + " is already taken");
            }
            user.setEmail(updateRequest.getEmail());
        }

        if (updateRequest.getUsername() != null) {
            if (userRepository.existsByUsername(updateRequest.getUsername())) {
                throw new UserAlreadyExistsException("Username '" + updateRequest.getUsername() + "' already exists");
            }
            user.setUsername(updateRequest.getUsername());
        }

        if (updateRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        User updatedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(updatedUser.getId())
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .role(updatedUser.getRole())
                .build();
    }

}
