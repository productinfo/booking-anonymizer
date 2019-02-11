package named.entity;

import java.util.HashMap;

public interface NamedEntityExtractor {

    HashMap<String, NERType> getNamedEntitiesAndPreprocessedText(final String text);
}
