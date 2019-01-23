package co.reference.resolution;

import named.entity.recognition.NamedEntityRecognizer;

import java.util.List;

public class CoReferenceResolverImp implements CoReferenceResolver {

    private final NamedEntityRecognizer namedEntityRecognizer;

    CoReferenceResolverImp(NamedEntityRecognizer namedEntityRecognizer) {
        this.namedEntityRecognizer = namedEntityRecognizer;
    }

    /**
     * TODO Implement Co-Reference Resolver
     */
    public List<List<String>> getResolvedReferencedNamedEntities(List<String> text) {
        return null;
    }
}
