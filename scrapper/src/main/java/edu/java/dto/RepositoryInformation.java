package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RepositoryInformation {
    private String name;
    @JsonSetter("pushed_at")
    private OffsetDateTime lastUpdateTime;
    private GithubActivity lastActivity;
}
