package org.example.codesix.domain.card.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.codesix.domain.workspace.entity.Member;
import org.example.codesix.global.entity.BaseEntity;

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

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}
