package named.entity.recognition;

import named.entity.NERType;

import java.util.HashMap;
import java.util.Map;

public abstract class NamedEntityRecognizer {

    public abstract HashMap<String, NERType> getNamedEntities(String text);

    protected final static Map<String, NERType> NER_TYPES;

    static {
        NER_TYPES = new HashMap<>();
        NER_TYPES.put("PERSON", NERType.PERSON);
        NER_TYPES.put("LOCATION", NERType.LOCATION);
        NER_TYPES.put("ORGANIZATION", NERType.ORGANIZATION);
        NER_TYPES.put("MONEY", NERType.MONEY);
        NER_TYPES.put("DATE", NERType.DATE);
    }
}
