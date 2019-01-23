package co.reference.resolution;

import named.entity.recognition.NamedEntityRecognizer;

public class CoReferenceResolverFactory {

    private static CoReferenceResolver coReferenceResolver = null;

    public static CoReferenceResolver getCoReferenceResolver(NamedEntityRecognizer namedEntityRecognizer) {
        if (coReferenceResolver == null) {
            coReferenceResolver = new CoReferenceResolverImp(namedEntityRecognizer);
        }
        return coReferenceResolver;
    }
}
