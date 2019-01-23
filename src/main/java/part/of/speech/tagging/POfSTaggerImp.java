package part.of.speech.tagging;

import constants.PartsOfSpeech;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.apache.commons.lang3.StringUtils;
import preprocessing.Preprocessor;

import java.util.*;

public class POfSTaggerImp implements POfSTagger {

    private final Preprocessor preprocessor;
    private final POSTaggerME posTaggerME;
    private static POfSTagger pOfSTagger = null;

    private POfSTaggerImp(final Preprocessor preprocessor, final POSTaggerME posTaggerME) {
        this.preprocessor = Objects.requireNonNull(preprocessor, "Preprocessor can't be null");
        this.posTaggerME = Objects.requireNonNull(posTaggerME, "POS Tagger can't be null");
    }

    public static POfSTagger getPOfSTagger(final Preprocessor preprocessor, final POSTaggerME posTaggerME) {
        if (pOfSTagger == null) {
            pOfSTagger = new POfSTaggerImp(preprocessor, posTaggerME);
        }
        return pOfSTagger;
    }

    @Override
    public String removeIrrelevantPartsOfSpeech(final String rawText) {
        Objects.requireNonNull(rawText, "Raw text cannot be null");
        final String preprocessedText = preprocessor.getPreprocessedText(rawText);
        final String[] preprocessedTokens = WhitespaceTokenizer.INSTANCE.tokenize(preprocessedText);
        final List<String> usefulTokens = new LinkedList<>();
        for (String preprocessedToken : preprocessedTokens) {
            if (wordIsUsefulPartOfSpeech(preprocessedToken)) {
                usefulTokens.add(preprocessedToken);
            }
        }
        return StringUtils.join(usefulTokens, " ");
    }

    private boolean wordIsUsefulPartOfSpeech(final String word) {
        final String[] partsOfSpeech = Optional.ofNullable(posTaggerME.tag(new String[]{word})).orElse(new String[]{});
        for (String partOfSpeech : partsOfSpeech) {
            if (PartsOfSpeech.UNWANTED_PARTS_OF_SPEECH.contains(partOfSpeech)) {
                return false;
            }
        }
        return true;
    }
}
