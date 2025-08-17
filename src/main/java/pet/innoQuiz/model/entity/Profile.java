package pet.innoQuiz.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String avatarUrl;
    private String bio;

    @OneToMany(mappedBy = "author")
    private List<Quiz> createdQuizzes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<QuizResult> passedQuizzes = new ArrayList<>();

    /*
    @ManyToMany
    @JoinTable(
            name = "profile_achievements",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "achievement_id")
    )
    private Set<Achievement> achievements = new HashSet<>();
    */
}
