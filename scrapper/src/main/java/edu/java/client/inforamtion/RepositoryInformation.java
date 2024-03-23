package edu.java.client.inforamtion;

import com.fasterxml.jackson.annotation.JsonSetter;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RepositoryInformation {
    private String name;
    @JsonSetter("pushed_at")
    private OffsetDateTime lastUpdateTime;
    private GithubActivity lastActivity;

    public enum GithubActivity {
        PUSH("добавлен новый комментарий"),
        BRANCH_CREATION("создана новая ветка"),
        UNKNOWN("неизвестная активность");

        public final String description;

        GithubActivity(String description) {
            this.description = description;
        }
    }
}
