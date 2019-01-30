package named.entity.recognition;

import java.util.List;

public interface NamedEntityRecognizer {

    List<String> getNamedEntities(String text);
}
