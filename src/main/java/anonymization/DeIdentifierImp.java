package anonymization;

import named.entity.NERType;
import named.entity.NamedEntityExtractor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class DeIdentifierImp implements DeIdentifier {

    private final NamedEntityExtractor namedEntityExtractor;
    private final static HashSet<NERType> SENSIBLE_NER_TYPES;
    private final static String ANONYMIZED_ENITY_PREFIX = " {@object:";
    private final static String ANONYMIZED_ENITY_SUFFIX = "} ";

    static {
        SENSIBLE_NER_TYPES = new HashSet<>();
        SENSIBLE_NER_TYPES.add(NERType.PERSON);
        SENSIBLE_NER_TYPES.add(NERType.MONEY);
        SENSIBLE_NER_TYPES.add(NERType.DATE);
        SENSIBLE_NER_TYPES.add(NERType.MISC);
    }

    public DeIdentifierImp(final NamedEntityExtractor namedEntityExtractor) {
        this.namedEntityExtractor = Objects.requireNonNull(namedEntityExtractor, "Named Entity Recognizer can't be null");
    }

    public String getDeIdentifiedText(final String rawText) {
        Objects.requireNonNull(rawText, "Raw text cannot be null");
        final HashMap<String, NERType> namedEntities = namedEntityExtractor.getNamedEntitiesAndPreprocessedText(rawText);
        String preprocessedText = getTextWithoutDigits(rawText);
        for (Map.Entry<String, NERType> namedEntity : namedEntities.entrySet()) {
            String entity = namedEntity.getKey();
            NERType entityType = namedEntity.getValue();
            preprocessedText = getTextWithDeIdentifiedEntity(preprocessedText, entity, entityType);
        }
        return StringUtils.normalizeSpace(preprocessedText);
    }

    private String getTextWithDeIdentifiedEntity(String text, String entity, NERType nerType) {
        final String searchPattern = "\\b(?i)" + Pattern.quote(entity);
        String replaceToken = ANONYMIZED_ENITY_PREFIX + nerType.name();
        if(!SENSIBLE_NER_TYPES.contains(nerType)){
            replaceToken = replaceToken + ":" + StringUtils.capitalize(entity);
        }
        replaceToken = replaceToken + ANONYMIZED_ENITY_SUFFIX;
        return text.replaceAll(searchPattern, replaceToken);
    }

    private String getTextWithoutDigits(final String rawText) {
        return rawText.replaceAll("\\p{N}", " ");
    }
}
