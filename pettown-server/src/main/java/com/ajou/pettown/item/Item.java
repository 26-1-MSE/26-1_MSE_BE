package com.ajou.pettown.item;

// Entity representing a stack of a specific item type owned by a user.
import com.ajou.pettown.auth.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer itemTypeId; // 1-4: food types, 5: water

    @Builder.Default
    @Column(nullable = false)
    private Integer count = 0; // total quantity held

    public void addCount(int amount) { this.count += amount; }
    public void useOne() { this.count--; }
}
