package anonymization;

import co.reference.resolution.CoReferenceResolver;

public class DeIdentifierFactory {

    private static DeIdentifier deIdentifier = null;

    public static DeIdentifier getDeIdentifier(CoReferenceResolver coReferenceResolver) {
        if (deIdentifier == null) {
            deIdentifier = new DeIdentifierImp(coReferenceResolver);
        }
        return deIdentifier;
    }
}
