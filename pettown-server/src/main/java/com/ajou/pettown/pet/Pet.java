package com.ajou.pettown.pet;

// Entity representing a pet owned by a user, with level and stat tracking.
import com.ajou.pettown.auth.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer petTypeId;

    @Column(nullable = false)
    private Integer petIndex; // 1-based acquisition order per user (1~4), used for name mapping

    @Builder.Default
    @Column(nullable = false)
    private Integer level = 1;

    @Builder.Default
    @Column(nullable = false)
    private Integer food = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer water = 0;

    public void feedFood() { this.food++; }
    public void drinkWater() { this.water++; }

    // Level up resets food and water to 0 for the next level's requirements
    public void levelUp() { this.level++; this.food = 0; this.water = 0; }
}
