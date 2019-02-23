package preprocessing;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class PreprocessorImp implements Preprocessor {

    public String getPreprocessedText(final String rawText) {
        Objects.requireNonNull(rawText, "Raw text cannot be null");
        String preprocessedText = rawText.replaceAll("\\b(?i)EC\\b", " ");
        return StringUtils.normalizeSpace(preprocessedText);
    }
}
