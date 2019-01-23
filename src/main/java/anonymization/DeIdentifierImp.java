package anonymization;

import named.entity.recognition.NamedEntityRecognizer;

import java.util.Objects;

public class DeIdentifierImp implements DeIdentifier {

    private final NamedEntityRecognizer namedEntityRecognizer;
    private static DeIdentifier deIdentifier = null;

    private DeIdentifierImp(final NamedEntityRecognizer namedEntityRecognizer) {
        this.namedEntityRecognizer = Objects.requireNonNull(namedEntityRecognizer, "Named Entity Recognizer can't be null");
    }

    /**
     * TODO Implement De-Identifier
     */
    public String getDeIdentifiedText(final String rawText) {
        Objects.requireNonNull(rawText, "Raw text cannot be null");
        return null;
    }

    public static DeIdentifier getDeIdentifier(final NamedEntityRecognizer namedEntityRecognizer) {
        if (deIdentifier == null) {
            deIdentifier = new DeIdentifierImp(namedEntityRecognizer);
        }
        return deIdentifier;
    }
}
