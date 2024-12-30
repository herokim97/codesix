package org.example.codesix.domain.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.codesix.domain.board.color.backgroundColor;
import org.example.codesix.domain.workspace.entity.Workspace;

@Entity
@Getter
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;


    private String backgroundColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace _ id")
    private Workspace workspace;



    //@OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    //private List<List> list = new ArrayList<>();

    public Board(Workspace workspace, String title, String backgroundColor) {
        this.workspace = workspace;
        this.title = title;
        this.backgroundColor = backgroundColor;
    }

    public void updateBoard(String title, String backgroundColor) {
        if(title!=null && !title.isEmpty()){
            this.title = title;
        }
        if(backgroundColor!=null ){
            this.backgroundColor = backgroundColor;
        }

        }
    }

