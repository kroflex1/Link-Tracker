package edu.java.dao.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "links")
@Getter
@Setter
@NoArgsConstructor
public class Link {
    @Id
    private String link;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "last_check_time", nullable = false)
    private OffsetDateTime lastCheckTime;

    @Column(name = "last_activity_time", nullable = false)
    private OffsetDateTime lastActivityTime;

    @ManyToMany(mappedBy = "trackedLinks", fetch = FetchType.LAZY)
    private Set<Chat> chats = new HashSet<>();

    public Link(String link, OffsetDateTime createdAt, OffsetDateTime lastCheckTime, OffsetDateTime lastActivityTime) {
        this.link = link;
        this.createdAt = createdAt;
        this.lastCheckTime = lastCheckTime;
        this.lastActivityTime = lastActivityTime;
    }
}
