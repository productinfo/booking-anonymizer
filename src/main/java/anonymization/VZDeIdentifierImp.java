package anonymization;

import named.entity.NERType;
import named.entity.NamedEntityExtractor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class VZDeIdentifierImp implements DeIdentifier {

    private final NamedEntityExtractor namedEntityExtractor;
    protected final static String ANONYMIZED_ENITY_PREFIX = " {@object:";
    protected final static String ANONYMIZED_ENITY_SUFFIX = "} ";
    private final HashSet<NERType> sensibleNerTypes;


    public VZDeIdentifierImp(final NamedEntityExtractor namedEntityExtractor) {
        this.namedEntityExtractor = Objects.requireNonNull(namedEntityExtractor, "Named Entity Recognizer can't be null");
        sensibleNerTypes = getSensibleNERTypes();
    }

    public String getDeIdentifiedText(final String rawText) {
        if (rawText == null) {
            return "";
        }
        final HashMap<String, NERType> namedEntities = namedEntityExtractor.getNamedEntitiesAndPreprocessedText(rawText);
        String preprocessedText = getTextWithoutDigits(rawText);
        final HashSet<String> entitiesProcessed = new HashSet<>();
        for (Map.Entry<String, NERType> namedEntity : namedEntities.entrySet()) {
            String entity = namedEntity.getKey();
            NERType entityType = namedEntity.getValue();
            preprocessedText = getTextWithDeIdentifiedEntity(preprocessedText, entity, entityType, entitiesProcessed);
        }
        return StringUtils.normalizeSpace(preprocessedText);
    }

    protected HashSet<NERType> getSensibleNERTypes() {
        HashSet<NERType> sensibleNerTypes = new HashSet<>();
        sensibleNerTypes.add(NERType.PERSON);
        sensibleNerTypes.add(NERType.MONEY);
        sensibleNerTypes.add(NERType.DATE);
        sensibleNerTypes.add(NERType.MISC);
        sensibleNerTypes.add(NERType.URL);
        sensibleNerTypes.add(NERType.LOCATION);
        sensibleNerTypes.add(NERType.EMAIL);
        return sensibleNerTypes;
    }

    protected String getTextWithDeIdentifiedEntity(String text, String entity, NERType nerType, HashSet<String> entitiesProcessed) {

        if (entityAlreadyProcessed(entitiesProcessed, entity)) {
            return text;
        }

        final String searchPattern = "(?i)" + Pattern.quote(entity);
        String replaceToken = ANONYMIZED_ENITY_PREFIX + nerType.name();

        if (!sensibleNerTypes.contains(nerType)) {
            replaceToken = replaceToken + ":" + StringUtils.capitalize(entity);
        }

        replaceToken = replaceToken + ANONYMIZED_ENITY_SUFFIX;
        String processedText = text.replaceAll(searchPattern, replaceToken);
        entitiesProcessed.add(entity);
        return processedText;
    }

    protected boolean entityAlreadyProcessed(HashSet<String> entitiesProcessed, String entity) {
        for (String processedEntity : entitiesProcessed) {
            if (processedEntity.equalsIgnoreCase(entity) || processedEntity.contains(entity)) {
                return true;
            }
        }
        return false;
    }

    private String getTextWithoutDigits(final String rawText) {
        return rawText.replaceAll("\\p{N}", "#");
    }
}
