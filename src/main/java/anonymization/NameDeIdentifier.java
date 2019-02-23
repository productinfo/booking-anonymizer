package anonymization;

import named.entity.NERType;
import named.entity.NamedEntityExtractor;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashSet;
import java.util.regex.Pattern;

public class NameDeIdentifier extends VZDeIdentifierImp {

    private final HashSet<NERType> sensibleNerTypes;

    public NameDeIdentifier(NamedEntityExtractor namedEntityExtractor) {
        super(namedEntityExtractor);
        sensibleNerTypes = getSensibleNERTypes();
    }

    protected HashSet<NERType> getSensibleNERTypes() {
        HashSet<NERType> sensibleNerTypes = new HashSet<>();
        sensibleNerTypes.add(NERType.PERSON);
        return sensibleNerTypes;
    }

    protected String getTextWithDeIdentifiedEntity(String text, String entity, NERType nerType, HashSet<String> entitiesProcessed) {
        if (entityAlreadyProcessed(entitiesProcessed, entity)) {
            return text;
        }

        final String searchPattern = "(?i)" + Pattern.quote(entity);

        if (!sensibleNerTypes.contains(nerType)) {
            return text;
        }
        String replaceToken = ANONYMIZED_ENITY_PREFIX + nerType.name() + ":HASH:" + DigestUtils.md2Hex(entity) + ANONYMIZED_ENITY_SUFFIX;
        String processedText = text.replaceAll(searchPattern, replaceToken);
        entitiesProcessed.add(entity);
        return processedText;
    }
}
