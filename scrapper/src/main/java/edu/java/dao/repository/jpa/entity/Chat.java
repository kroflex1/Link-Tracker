package edu.java.dao.repository.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
public class Chat {

    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "link_and_chat",
               joinColumns = @JoinColumn(name = "link"),
               inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private Set<Link> trackedLinks = new HashSet<>();

    public Chat(Long chatId, OffsetDateTime createdAt) {
        this.chatId = chatId;
        this.createdAt = createdAt;
    }

    public void addLink(Link link) {
        trackedLinks.add(link);
        link.getChats().add(this);
    }

    public void removeLink(Link link){
        trackedLinks.remove(link);
        link.getChats().remove(this);
    }
}
