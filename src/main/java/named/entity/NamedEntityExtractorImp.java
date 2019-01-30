package named.entity;

import named.entity.recognition.NamedEntityRecognizer;
import org.apache.commons.lang3.tuple.ImmutablePair;
import part.of.speech.tagging.POfSTagger;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public class NamedEntityExtractorImp implements NamedEntityExtractor {

    private final POfSTagger pOfSTagger;
    private final List<NamedEntityRecognizer> namedEntityRecognizers;

    public NamedEntityExtractorImp(final POfSTagger pOfSTagger, List<NamedEntityRecognizer> namedEntityRecognizers) {
        this.namedEntityRecognizers = Objects.requireNonNull(namedEntityRecognizers, "NamedEntityRecognizer list cannot be null");
        this.pOfSTagger = Objects.requireNonNull(pOfSTagger, "Part Of Speech Tagger cannot be null");
    }

    @Override
    public ImmutablePair<String, HashSet<String>> getNamedEntitiesAndPreprocessedText(final String text) {
        HashSet<String> namedEntities = new LinkedHashSet<>();
        String preprocessedText = pOfSTagger.removeIrrelevantPartsOfSpeech(text);
        for (NamedEntityRecognizer namedEntityRecognizer : namedEntityRecognizers) {
            namedEntities.addAll(namedEntityRecognizer.getNamedEntities(text));
        }
        return new ImmutablePair<>(preprocessedText, namedEntities);
    }
}
