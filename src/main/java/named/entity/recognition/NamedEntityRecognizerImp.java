package named.entity.recognition;

import part.of.speech.tagging.POfSTagger;

import java.util.List;
import java.util.Objects;

public class NamedEntityRecognizerImp implements NamedEntityRecognizer {

    private final POfSTagger pOfSTagger;
    private static NamedEntityRecognizer namedEntityRecognizer = null;

    private NamedEntityRecognizerImp(final POfSTagger pOfSTagger) {
        this.pOfSTagger = Objects.requireNonNull(pOfSTagger, "Part Of Speech Tagger can't be null");
    }

    /**
     * TODO Implement Named Entity Recognizer
     */
    public List<String> getNamedEntitiesFromText(final String rawText) {
        Objects.requireNonNull(rawText, "Raw text text cannot be null");
        return null;
    }

    public static NamedEntityRecognizer getNamedEntityRecognizer(final POfSTagger pOfSTagger) {
        if (namedEntityRecognizer == null) {
            namedEntityRecognizer = new NamedEntityRecognizerImp(pOfSTagger);
        }
        return namedEntityRecognizer;
    }
}
