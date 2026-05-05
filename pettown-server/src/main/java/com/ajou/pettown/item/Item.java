package com.ajou.pettown.item;

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
    private Integer itemTypeId;

    @Builder.Default
    @Column(nullable = false)
    private Integer count = 0;

    public void addCount(int amount) { this.count += amount; }
    public void useOne() { this.count--; }
}
