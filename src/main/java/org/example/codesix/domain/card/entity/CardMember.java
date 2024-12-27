package org.example.codesix.domain.card.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.codesix.domain.comment.entity.Comment;
import org.example.codesix.domain.workspace.entity.Member;
import org.example.codesix.global.entity.BaseEntity;

import java.util.List;

@Entity
@Getter
@Setter
public class CardMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "cardMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

}
