package org.example.codesix.domain.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.codesix.domain.card.entity.Card;

import java.util.List;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "card_id",nullable = false)
    private Card card;

    @Column
    private String comment;

}
