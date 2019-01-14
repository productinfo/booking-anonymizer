package preprocessing;

import java.util.List;

public interface Preprocessor {

    List<String> getPreprocessedText(List<String> rawText);
}
