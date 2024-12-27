package org.example.codesix.domain.worklist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.codesix.domain.board.entity.Board;
import org.example.codesix.global.entity.BaseEntity;

@NoArgsConstructor
@Entity
@Getter
public class WorkList extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;

    private String content;

    private Integer sequence = 1;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

//    @OneToMany(mappedBy = "List", cascade = CascadeType.REMOVE)
//    private List<Card> cards = new ArrayList<>();

    public WorkList(Board board, String title, String content) {
        this.board = board;
        this.title = title;
        this.content = content;
    }
    public void updateList(Long id, String title){
        this.id = id;
        this.title = title;
    }

}
