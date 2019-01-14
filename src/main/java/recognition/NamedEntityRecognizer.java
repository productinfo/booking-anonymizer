package recognition;

import java.util.List;

public interface NamedEntityRecognizer {

    List<List<String>> getNamedEntitiesFromText(List<String> text);
}
