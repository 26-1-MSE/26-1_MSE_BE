package com.ajou.pettown.pet;

import com.ajou.pettown.auth.User;
import jakarta.persistence.*;

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
    private Integer level = 1;

    @Column(nullable = false)
    private Integer food = 0;

    @Column(nullable = false)
    private Integer water = 0;

    public static PetBuilder builder() { return new PetBuilder(); }

    public Long getPetId() { return petId; }
    public User getUser() { return user; }
    public Integer getPetTypeId() { return petTypeId; }
    public Integer getLevel() { return level; }
    public Integer getFood() { return food; }
    public Integer getWater() { return water; }

    public static class PetBuilder {
        private User user;
        private Integer petTypeId;
        private Integer level = 1;
        private Integer food = 0;
        private Integer water = 0;

        public PetBuilder user(User user) { this.user = user; return this; }
        public PetBuilder petTypeId(Integer petTypeId) { this.petTypeId = petTypeId; return this; }
        public PetBuilder level(Integer level) { this.level = level; return this; }
        public PetBuilder food(Integer food) { this.food = food; return this; }
        public PetBuilder water(Integer water) { this.water = water; return this; }

        public Pet build() {
            Pet pet = new Pet();
            pet.user = this.user;
            pet.petTypeId = this.petTypeId;
            pet.level = this.level;
            pet.food = this.food;
            pet.water = this.water;
            return pet;
        }
    }
}