package named.entity;

import named.entity.recognition.NamedEntityRecognizer;
import preprocessing.Preprocessor;

import java.util.*;

public class NamedEntityExtractorImp implements NamedEntityExtractor {

    private final Preprocessor preprocessor;
    private final List<NamedEntityRecognizer> namedEntityRecognizers;

    public NamedEntityExtractorImp(final Preprocessor preprocessor, List<NamedEntityRecognizer> namedEntityRecognizers) {
        this.namedEntityRecognizers = Objects.requireNonNull(namedEntityRecognizers, "NamedEntityRecognizer list cannot be null");
        this.preprocessor = Objects.requireNonNull(preprocessor, "PreprocessorTagger cannot be null");
    }

    @Override
    public HashMap<String, NERType> getNamedEntitiesAndPreprocessedText(final String text) {
        Objects.requireNonNull(text, "text cannot be null");
        final HashMap<String, NERType> namedEntities = new HashMap<>();
        final String preprocessedText = preprocessor.getPreprocessedText(text);
        for (NamedEntityRecognizer namedEntityRecognizer : namedEntityRecognizers) {
            namedEntities.putAll(namedEntityRecognizer.getNamedEntities(preprocessedText));
        }
        return namedEntities;
    }
}
