package org.example.codesix.domain.card.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.codesix.domain.worklist.entity.WorkList;
import org.example.codesix.global.entity.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
}
