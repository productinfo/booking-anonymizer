package co.reference.resolution;

import java.util.List;

public interface CoReferenceResolver {

    List<List<String>> getResolvedReferencedNamedEntities(List<String> text);
}
