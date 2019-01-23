package part.of.speech.tagging;

import constants.PartsOfSpeech;
import initialization.DependencyBinder;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.apache.commons.lang3.StringUtils;
import preprocessing.Preprocessor;
import preprocessing.PreprocessorImp;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class POfSTaggerImp implements POfSTagger {

    private final Preprocessor preprocessor;
    private final POSTaggerME posTaggerME;
    private static POfSTagger pOfSTagger = null;

    private POfSTaggerImp(Preprocessor preprocessor, POSTaggerME posTaggerME) {
        this.preprocessor = Objects.requireNonNull(preprocessor, "Preprocessor can't be null");
        this.posTaggerME = Objects.requireNonNull(posTaggerME, "POS Tagger can't be null");
    }

    public static POfSTagger getPOfSTagger(Preprocessor preprocessor, POSTaggerME posTaggerME){
        if(pOfSTagger == null){
            pOfSTagger = new POfSTaggerImp(preprocessor, posTaggerME);
        }
        return pOfSTagger;
    }

    @Override
    public String removeIrrelevantPartsOfSpeech(String rawText) {
        String[] rawTextTokens = WhitespaceTokenizer.INSTANCE.tokenize(rawText);
        List<String> usefulTokens = new LinkedList<>();
        for(String rawToken: rawTextTokens){
            if(wordIsUsefulPartOfSpeech(rawToken)){
                usefulTokens.add(rawToken);
            }
        }
        return StringUtils.join(usefulTokens, " ");
    }

    private boolean wordIsUsefulPartOfSpeech(String word) {
        String[] partsOfSpeech = Optional.ofNullable(posTaggerME.tag(new String[]{word})).orElse(new String[]{});
        for (String partOfSpeech: partsOfSpeech){
            System.out.println(word + " : : " + partOfSpeech);
            if(PartsOfSpeech.UNWANTED_PARTS_OF_SPEECH.contains(partOfSpeech)){
                return false;
            }
        }
        return true;
    }
}
