package anonymization;

import com.google.common.hash.HashFunction;
import named.entity.NERType;
import named.entity.NamedEntityExtractor;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Pattern;

public class NameDeIdentifier extends VZDeIdentifierImp {

    private final HashSet<NERType> sensibleNerTypes;
    private final HashFunction hashFunction;

    public NameDeIdentifier(NamedEntityExtractor namedEntityExtractor, HashFunction hashFunction) {
        super(namedEntityExtractor);
        this.hashFunction = Objects.requireNonNull(hashFunction, "hash function cannot be null");
        sensibleNerTypes = getSensibleNERTypes();
    }

    protected HashSet<NERType> getSensibleNERTypes(){
        HashSet<NERType> sensibleNerTypes = new HashSet<>();
        sensibleNerTypes.add(NERType.PERSON);
        return sensibleNerTypes;
    }

    protected String getTextWithDeIdentifiedEntity(String text, String entity, NERType nerType) {
        final String searchPattern = "(?i)" + Pattern.quote(entity);
        if(!sensibleNerTypes.contains(nerType)){
            return text;
        }
        String replaceToken = ANONYMIZED_ENITY_PREFIX + nerType.name() + ":HASH:" + hashFunction.hashString(entity) + ANONYMIZED_ENITY_SUFFIX;
        return text.replaceAll(searchPattern, replaceToken);
    }
}
