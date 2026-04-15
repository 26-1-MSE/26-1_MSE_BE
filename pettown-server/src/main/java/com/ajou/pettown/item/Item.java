package com.ajou.pettown.item;

import com.ajou.pettown.auth.User;
import jakarta.persistence.*;

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

    @Column(nullable = false)
    private Integer count = 0;

    public static ItemBuilder builder() { return new ItemBuilder(); }

    public Long getItemId() { return itemId; }
    public User getUser() { return user; }
    public Integer getItemTypeId() { return itemTypeId; }
    public Integer getCount() { return count; }

    public static class ItemBuilder {
        private User user;
        private Integer itemTypeId;
        private Integer count = 0;

        public ItemBuilder user(User user) { this.user = user; return this; }
        public ItemBuilder itemTypeId(Integer itemTypeId) { this.itemTypeId = itemTypeId; return this; }
        public ItemBuilder count(Integer count) { this.count = count; return this; }

        public Item build() {
            Item item = new Item();
            item.user = this.user;
            item.itemTypeId = this.itemTypeId;
            item.count = this.count;
            return item;
        }
    }
}