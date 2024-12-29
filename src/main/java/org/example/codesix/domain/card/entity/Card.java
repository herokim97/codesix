package org.example.codesix.domain.card.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.codesix.domain.comment.entity.Comment;
import org.example.codesix.domain.worklist.entity.WorkList;
import org.example.codesix.global.entity.BaseEntity;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "work_list_id", nullable = false)
    private WorkList workList;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private LocalDate dueDate;

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CardMember> cardMembers = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CardHistory> histories = new HashSet<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    public Card(WorkList workList, String title, String description, LocalDate dueDate) {
        this.workList = workList;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public void update(String title, String description, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public void addHistory(CardHistory history) {
        this.histories.add(history);
        history.setCard(this); // 양방향 관계 설정
    }

    public Collection<Comment> getComments() {
        return comments;
    }
}
