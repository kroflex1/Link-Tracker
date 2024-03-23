package edu.java.utils.linkInformant;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Optional;

public abstract class LinkInformant {
    LinkInformant next;

    public void setNextLinkUpdateDescription(LinkInformant next) {
        this.next = next;
    }

    public Optional<LinkActivityInformation> getLinkActivityInformationAfterDate(
        OffsetDateTime lastUpdateDate,
        URI url
    ) {
        Optional<LinkActivityInformation> activityInformation = getLinkActivityInformation(lastUpdateDate, url);
        if (activityInformation.isEmpty()) {
            next.getLinkActivityInformationAfterDate(lastUpdateDate, url);
        }
        return activityInformation;
    }

    protected abstract Optional<LinkActivityInformation> getLinkActivityInformation(
        OffsetDateTime lastUpdateDate,
        URI url
    );

    public record LinkActivityInformation(OffsetDateTime lastActivityTime, String message) {

    }
}
