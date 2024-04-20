package edu.java.utils.linkInformant;

import edu.java.client.GitHubClient;
import edu.java.client.inforamation.RepositoryInformation;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Optional;

public class GithubLinkInformant extends LinkInformant {
    private final GitHubClient gitHubClient;

    public GithubLinkInformant(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @Override
    protected Optional<LinkActivityInformation> getLinkActivityInformation(OffsetDateTime lastUpdateDate, URI url) {
        Optional<RepositoryInformation> repositoryInformation = gitHubClient.getRepositoryInformation(url);
        if (repositoryInformation.isEmpty()
            || repositoryInformation.get().getLastUpdateTime().isBefore(lastUpdateDate)) {
            return Optional.empty();
        }
        RepositoryInformation repository = repositoryInformation.get();
        String message = String.format(
            "В репозитории %s произошло изменение: %s",
            repository.getName(),
            repository.getLastActivity().description
        );
        LinkActivityInformation linkActivityInformation =
            new LinkActivityInformation(repository.getLastUpdateTime(), message);
        return Optional.of(linkActivityInformation);
    }
}
