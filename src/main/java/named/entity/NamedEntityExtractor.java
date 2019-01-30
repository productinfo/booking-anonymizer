package named.entity;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashSet;

public interface NamedEntityExtractor {

    ImmutablePair<String, HashSet<String>> getNamedEntitiesAndPreprocessedText(final String text);
}
