package org.example.codesix.domain.worklist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.codesix.domain.board.entity.Board;
import org.example.codesix.domain.card.entity.Card;
import org.example.codesix.global.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
public class WorkList extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private Integer sequence = 1;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "workList", cascade = CascadeType.REMOVE)
    private List<Card> cards = new ArrayList<>();

    public WorkList(Board board, String title) {
        this.board = board;
        this.title = title;
    }

    public void updateList(String title){
        this.title = title;
    }


}
