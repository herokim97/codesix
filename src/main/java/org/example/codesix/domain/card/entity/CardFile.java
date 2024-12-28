package org.example.codesix.domain.card.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "card_file")
public class CardFile{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(nullable = false)
    private String url; // S3 URL

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private String extension;

    @Column(nullable = false)
    private String filename; // 파일 이름

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public CardFile(Card card, String url, Long size, String extension, String filename) {
        this.card = card;
        this.url = url;
        this.size = size;
        this.extension = extension;
        this.filename = filename;
    }
}
