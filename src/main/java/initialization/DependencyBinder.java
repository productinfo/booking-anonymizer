package initialization;

import anonymization.DeIdentifier;
import anonymization.DeIdentifierImp;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import named.entity.NamedEntityExtractor;
import named.entity.NamedEntityExtractorImp;
import named.entity.recognition.NamedEntityRecognizer;
import named.entity.recognition.OpenNLPRecognizer;
import named.entity.recognition.StanfordCoreRecognizer;
import named.entity.tokenization.TokenizerDecorator;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import part.of.speech.tagging.POfSTagger;
import part.of.speech.tagging.POfSTaggerImp;
import preprocessing.Preprocessor;
import preprocessing.PreprocessorImp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DependencyBinder {

    private static final POSModel POS_MODEL = getPOSModel();
    private static final List<NamedEntityRecognizer> NAMED_ENTITY_RECOGNIZERS = getNamedEntityRecognizers();

    public static DeIdentifier getAnonymizer() {
        final Preprocessor preprocessor = new PreprocessorImp();
        final POSTaggerME posTaggerME = new POSTaggerME(POS_MODEL);
        final POfSTagger POfSTagger = new POfSTaggerImp(preprocessor, posTaggerME);
        final NamedEntityExtractor namedEntityExtractor = new NamedEntityExtractorImp(POfSTagger, NAMED_ENTITY_RECOGNIZERS);
        return new DeIdentifierImp(namedEntityExtractor);
    }

    private static CRFClassifier<CoreLabel> getCRFClassifier(String modelName) {
        final String loadPath = DependencyBinder.class.getResource(modelName).getPath();
        return CRFClassifier.getClassifierNoExceptions(loadPath);
    }

    private static POSModel getPOSModel() {
        final InputStream modelInputStream = DependencyBinder.class.getResourceAsStream("/de-pos-maxent.bin");
        try {
            return new POSModel(modelInputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load Part Of Speech (POS) Model", e);
        }
    }

    private static NameFinderME getNameFinder(String nameFinderModelName) {
        final InputStream modelInputStream = DependencyBinder.class.getResourceAsStream(nameFinderModelName);
        try {
            TokenNameFinderModel tokenNameFinderModel = new TokenNameFinderModel(modelInputStream);
            return new NameFinderME(tokenNameFinderModel);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load Name Finder Moder Model", e);
        }
    }

    private static TokenizerModel getTokenizerModel(String tokenizerModelName) {
        final InputStream modelInputStream = DependencyBinder.class.getResourceAsStream(tokenizerModelName);
        try {
            return new TokenizerModel(modelInputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load Tokenizer Model", e);
        }
    }

    private static Tokenizer getTokenizer(String tokenizerModelName) {
        return new TokenizerME(getTokenizerModel(tokenizerModelName));
    }

    private static Tokenizer getTokenizerDecorator() {
        return new TokenizerDecorator(getTokenizerModel("/en-token.bin"));
    }


    private static List<NamedEntityRecognizer> getNamedEntityRecognizers() {
        final CRFClassifier<CoreLabel> germanClassifier = getCRFClassifier("/german.conll.germeval2014.hgc_175m_600.crf.ser.gz");
        final NamedEntityRecognizer germanStanfordNER = new StanfordCoreRecognizer(germanClassifier);
        final CRFClassifier<CoreLabel> englishClassifier = getCRFClassifier("/english.all.3class.caseless.distsim.crf.ser.gz");
        final NamedEntityRecognizer englishStanfordNER = new StanfordCoreRecognizer(englishClassifier);
        final CRFClassifier<CoreLabel> spanishClassifier = getCRFClassifier("/spanish.ancora.distsim.s512.crf.ser.gz");
        final NamedEntityRecognizer spanishStanfordNER = new StanfordCoreRecognizer(spanishClassifier);
        final NamedEntityRecognizer englishOpenNLPRecognizer = new OpenNLPRecognizer(getNameFinder("/en-ner-person.bin"), getTokenizer("/en-token.bin"));
        final NamedEntityRecognizer spanishOpenNLPRecognizer = new OpenNLPRecognizer(getNameFinder("/es-ner-person.bin"), getTokenizerDecorator());
        final NamedEntityRecognizer dutchOpenNLPRecognizer = new OpenNLPRecognizer(getNameFinder("/nl-ner-person.bin"), getTokenizer("/nl-token.bin"));
        return new LinkedList<>(Arrays.asList(germanStanfordNER, englishStanfordNER, spanishStanfordNER, englishOpenNLPRecognizer, spanishOpenNLPRecognizer, dutchOpenNLPRecognizer));
    }
}
