package anonymization;

import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import named.entity.NamedEntityExtractor;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Pattern;

public class DeIdentifierImp implements DeIdentifier {

    private final NamedEntityExtractor namedEntityExtractor;
    private static final HashFunction HASH_FUNCTION = Hashing.goodFastHash(32);

    public DeIdentifierImp(final NamedEntityExtractor namedEntityExtractor) {
        this.namedEntityExtractor = Objects.requireNonNull(namedEntityExtractor, "Named Entity Recognizer can't be null");
    }

    public String getDeIdentifiedText(final String rawText) {
        Objects.requireNonNull(rawText, "Raw text cannot be null");
        final ImmutablePair<String, HashSet<String>> textWithEntitiesPair = namedEntityExtractor.getNamedEntitiesAndPreprocessedText(rawText);
        String preprocessedText = textWithEntitiesPair.left;
        HashSet<String> entities = textWithEntitiesPair.right;
        for (String entity : entities) {
            preprocessedText = getTextWithDeIdentifiedEntity(preprocessedText, entity);
        }
        return preprocessedText;
    }

    private String getTextWithDeIdentifiedEntity(String text, String entity) {
        final String searchPattern = "\\b(?i)" + Pattern.quote(entity) + "\\b";
        final String anonymizeEntity = HASH_FUNCTION.hashString(entity.toLowerCase(), Charsets.UTF_8).toString();
        return text.replaceAll(searchPattern, anonymizeEntity);
    }
}
