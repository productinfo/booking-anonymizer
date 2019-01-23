package preprocessing;

import com.google.common.base.CaseFormat;
import constants.RelevantSEPAWord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import constants.IrrelevantWord;

import java.util.LinkedList;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class PreprocessorImp implements Preprocessor {

    private final static Preprocessor PREPROCESSOR = new PreprocessorImp();

    private PreprocessorImp(){}

    public static Preprocessor getPreprocessor(){
        return PREPROCESSOR;
    }

    public String getPreprocessedText(String rawText) {
        Objects.requireNonNull(rawText, "Raw text cannot be null");
        String preprocessedResult = removeAllPunctuationMarks(rawText);
        preprocessedResult = removeAllDigits(preprocessedResult);
        preprocessedResult = removeNonAlphabetical(preprocessedResult);
        preprocessedResult = removeSingleCharacterWords(preprocessedResult);
        preprocessedResult = removeIrrelevantSEPAWords(preprocessedResult);
        preprocessedResult = prepareTextForCamelCaseSplit(preprocessedResult);
        preprocessedResult = splitCamelCase(preprocessedResult);
        preprocessedResult = StringUtils.normalizeSpace(preprocessedResult);
        preprocessedResult = WordUtils.capitalize(preprocessedResult);
        return capitalizeRelevantSEPAWords(preprocessedResult);
    }

    private String removeAllPunctuationMarks(String rawText) {
        return replaceAllPunctuationMarks(rawText, "");
    }

    private String removeAllDigits(String rawText) {
        return rawText.replaceAll("\\p{N}", "");
    }

    private String removeNonAlphabetical(String rawText) {
        return rawText.replaceAll("\\p{Me}|\\p{S}", " ");
    }

    private String splitCamelCase(String rawText) {
        String preprocessedResult = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, rawText);
        return replaceAllPunctuationMarks(preprocessedResult, " ");
    }

    private String replaceAllPunctuationMarks(String rawText, String value) {
        return rawText.replaceAll("\\p{P}", value);
    }

    private String removeSingleCharacterWords(String rawText) {
        return rawText.replaceAll("\\b\\w\\b", "");
    }

    private String removeIrrelevantSEPAWords(String rawText) {
        String preprocessedResult = rawText;
        for (IrrelevantWord irrelevantWord : IrrelevantWord.values()) {
            preprocessedResult = preprocessedResult.replaceAll("\\b(?i)" + Pattern.quote(irrelevantWord.getWord()) + "\\b", "");
        }
        return preprocessedResult;
    }

    private String capitalizeRelevantSEPAWords(String rawText) {
        String preprocessedResult = rawText;
        for (RelevantSEPAWord relevantSEPAWord : RelevantSEPAWord.values()) {
            preprocessedResult = preprocessedResult.replaceAll("\\b(?i)" + Pattern.quote(relevantSEPAWord.getSEPAWord()) + "\\b", relevantSEPAWord.getSEPAWord());
        }
        return preprocessedResult;
    }

    private String prepareTextForCamelCaseSplit(String rawText) {
        StringTokenizer tokenizer = new StringTokenizer(rawText);
        String preprocessedResult = rawText;
        while (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            if (StringUtils.isAllUpperCase(token)) {
                preprocessedResult = preprocessedResult.replaceAll(token, token.toLowerCase());
            }
        }
        return preprocessedResult;
    }
}
