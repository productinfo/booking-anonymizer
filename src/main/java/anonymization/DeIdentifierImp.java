package anonymization;

import co.reference.resolution.CoReferenceResolver;

import java.util.List;

public class DeIdentifierImp implements DeIdentifier {

    private final CoReferenceResolver coReferenceResolver;

    DeIdentifierImp(CoReferenceResolver coReferenceResolver){
        this.coReferenceResolver = coReferenceResolver;
    }
    /**
     * TODO Implement De-Identifier
     */
    public List<String> getDeIdentifiedText(List<String> text) {
        return null;
    }
}
