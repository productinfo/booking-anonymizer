package anonymization;

import named.entity.recognition.NamedEntityRecognizer;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class DeIdentifierImp implements DeIdentifier {

    private final NamedEntityRecognizer namedEntityRecognizer;
    private static DeIdentifier deIdentifier = null;

    private DeIdentifierImp(final NamedEntityRecognizer namedEntityRecognizer) {
        this.namedEntityRecognizer = Objects.requireNonNull(namedEntityRecognizer, "Named Entity Recognizer can't be null");
    }

    /**
     * TODO Implement De-Identifier Logic
     */
    public String getDeIdentifiedText(final String rawText) {
        Objects.requireNonNull(rawText, "Raw text cannot be null");
        return StringUtils.join(namedEntityRecognizer.getNamedEntitiesFromText(rawText), " ");
    }

    public static DeIdentifier getDeIdentifier(final NamedEntityRecognizer namedEntityRecognizer) {
        if (deIdentifier == null) {
            deIdentifier = new DeIdentifierImp(namedEntityRecognizer);
        }
        return deIdentifier;
    }
}
