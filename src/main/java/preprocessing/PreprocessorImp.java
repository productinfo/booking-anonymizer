package preprocessing;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class PreprocessorImp implements Preprocessor {

    public String getPreprocessedText(final String rawText) {
        Objects.requireNonNull(rawText, "Raw text cannot be null");
        String preprocessedResult = removeAllPunctuationMarks(rawText);
        preprocessedResult = removeNonAlphabetical(preprocessedResult);
        preprocessedResult = StringUtils.normalizeSpace(preprocessedResult);
        return preprocessedResult;
    }

    private String removeAllPunctuationMarks(final String rawText) {
        return rawText.replaceAll("\\p{P}", " ");
    }

    private String removeNonAlphabetical(final String rawText) {
        return rawText.replaceAll("\\p{Me}|\\p{S}", "");
    }
}
