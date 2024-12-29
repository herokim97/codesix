package org.example.codesix.domain.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.domain.card.entity.CardMember;
import org.example.codesix.global.entity.BaseEntity;

@Entity
@Getter
@Setter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne
    @JoinColumn(name = "card_member_id", nullable = false)
    private CardMember cardMember;


    @Column(nullable = false)
    private String content;

    public Comment(Card card, CardMember cardMember, String content) {
        this.card=card;
        this.cardMember=cardMember;
        this.content=content;
    }

    public Comment() {

    }

    public void update(String content) {
        this.content=content;
    }
}
