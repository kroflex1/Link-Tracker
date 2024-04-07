package edu.java.utils.linkInformant;

import edu.java.client.StackOverflowClient;
import edu.java.client.inforamation.QuestionInformation;
import io.micrometer.common.lang.Nullable;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Optional;

public class StackOverflowInformant extends LinkInformant {
    private final StackOverflowClient stackOverflowClient;

    public StackOverflowInformant(StackOverflowClient stackOverflowClient) {
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    protected Optional<LinkActivityInformation> getLinkActivityInformation(OffsetDateTime lastUpdateDate, URI url) {
        Optional<QuestionInformation> questionInformation = stackOverflowClient.getInformationAboutQuestion(url);
        if (questionInformation.isEmpty()) {
            return Optional.empty();
        }
        QuestionInformation question = questionInformation.get();
        StringBuilder description = new StringBuilder();
        OffsetDateTime answerActivityDate = null;
        OffsetDateTime commentActivityDate = null;

        if (question.getLastAnswer() != null && question.getLastAnswer().getCreationDate().isAfter(lastUpdateDate)) {
            description.append(
                String.format(
                    "Пользователь %s оставил ответ на ваш вопрос: %s",
                    question.getLastAnswer().getOwnerName(),
                    question.getLastAnswer().getText()
                )
            );
            answerActivityDate = question.getLastAnswer().getCreationDate();
        }
        if (question.getLastComment() != null && question.getLastComment().getCreationDate().isAfter(lastUpdateDate)) {
            description.append(
                String.format(
                    "Пользователь %s оставил комментарий к вашему вопросу: %s",
                    question.getLastComment().getOwnerName(),
                    question.getLastComment().getText()
                )
            );
            commentActivityDate = question.getLastComment().getCreationDate();
        }

        if (answerActivityDate == null && commentActivityDate == null) {
            return Optional.empty();
        }
        return Optional.of(new LinkActivityInformation(
            getMaxOffsetDateTime(answerActivityDate, commentActivityDate),
            description.toString()
        ));
    }

    private OffsetDateTime getMaxOffsetDateTime(
        @Nullable OffsetDateTime firstDate,
        @Nullable OffsetDateTime secondDate
    ) {
        if (firstDate == null) {
            return secondDate;
        }
        if (secondDate == null) {
            return firstDate;
        }
        if (firstDate.isAfter(secondDate)) {
            return firstDate;
        }
        return secondDate;
    }

}
