package initialization;

import anonymization.AmountDeIdentifier;
import anonymization.DeIdentifier;
import anonymization.NameDeIdentifier;
import anonymization.VZDeIdentifierImp;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import named.entity.NERType;
import named.entity.NamedEntityExtractor;
import named.entity.NamedEntityExtractorImp;
import named.entity.recognition.NamedEntityRecognizer;
import named.entity.recognition.OpenNLPRecognizer;
import named.entity.recognition.RegexRecognizer;
import named.entity.recognition.StanfordCoreRecognizer;
import named.entity.tokenization.TokenizerDecorator;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import preprocessing.PreprocessorImp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DependencyBinder {

    private static final List<NamedEntityRecognizer> NAMED_ENTITY_RECOGNIZERS;
    private static final int INITIAL_CAPACITY = 33;

    static {
        NAMED_ENTITY_RECOGNIZERS = getNamedEntityRecognizers();
    }

    public static DeIdentifier getVZDeIdentifier() {
        final NamedEntityExtractor namedEntityExtractor = new NamedEntityExtractorImp(new PreprocessorImp(), NAMED_ENTITY_RECOGNIZERS);
        return new VZDeIdentifierImp(namedEntityExtractor);
    }

    public static DeIdentifier getNameDeIdentifier() {
        final NamedEntityExtractor namedEntityExtractor = new NamedEntityExtractorImp(new PreprocessorImp(), NAMED_ENTITY_RECOGNIZERS);
        return new NameDeIdentifier(namedEntityExtractor);
    }

    public static DeIdentifier getAmountDeIdentifier(){
        return new AmountDeIdentifier();
    }

    private static CRFClassifier<CoreLabel> getCRFClassifier(String modelName) {
        final String loadPath = DependencyBinder.class.getResource(modelName).getPath();
        return CRFClassifier.getClassifierNoExceptions(loadPath);
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
        List<NamedEntityRecognizer> namedEntityRecognizers = new ArrayList<>(INITIAL_CAPACITY);
        namedEntityRecognizers.add(getOpenNLPEnglishMoneyNER());
        namedEntityRecognizers.add(getOpenNLPDutchLocationNER());
        namedEntityRecognizers.add(getOpenNLPEnglishLocationNER());
        namedEntityRecognizers.add(getOpenNLPSpanishLocationNER());
        namedEntityRecognizers.add(getGermanStanfordNER());
        namedEntityRecognizers.add(getEnglishStanfordNER());
        namedEntityRecognizers.add(getSpanishStanfordNER());
        namedEntityRecognizers.add(getOpenNLPDutchPersonNER());
        namedEntityRecognizers.add(getOpenNLPEnglishPersonNER());
        namedEntityRecognizers.add(getOpenNLPSpanishPersonNER());
        namedEntityRecognizers.add(getOpenNLPEnglishOrganizationNER());
        namedEntityRecognizers.add(getOpenNLPDutchOrganizationNER());
        namedEntityRecognizers.add(getOpenNLPSpanishOrganizationNER());
        namedEntityRecognizers.add(getOpenNLPEnglishDateNER());
        namedEntityRecognizers.add(getURLNER());
        namedEntityRecognizers.add(getEmailNER());
        return namedEntityRecognizers;
    }

    private static List<NamedEntityRecognizer> getAmountNamedEntityRecognizers() {
        //The use of an array list here in intention. Subsequent threads need to operate with array indexes
        List<NamedEntityRecognizer> namedEntityRecognizers = new ArrayList<>(INITIAL_CAPACITY);
        // Order Matters to the Implementation.
        // Do not change unless you know what you are doing.
        namedEntityRecognizers.add(getGermanStanfordNER());
        namedEntityRecognizers.add(getEnglishStanfordNER());
        namedEntityRecognizers.add(getSpanishStanfordNER());
        namedEntityRecognizers.add(getOpenNLPDutchPersonNER());
        namedEntityRecognizers.add(getOpenNLPEnglishPersonNER());
        namedEntityRecognizers.add(getOpenNLPSpanishPersonNER());
        return namedEntityRecognizers;
    }

    private static NamedEntityRecognizer getEnglishStanfordNER() {
        final CRFClassifier<CoreLabel> englishClassifier = getCRFClassifier("/english.all.3class.caseless.distsim.crf.ser.gz");
        return new StanfordCoreRecognizer(englishClassifier);
    }

    private static NamedEntityRecognizer getGermanStanfordNER() {
        final CRFClassifier<CoreLabel> germanClassifier = getCRFClassifier("/german.conll.germeval2014.hgc_175m_600.crf.ser.gz");
        return new StanfordCoreRecognizer(germanClassifier);
    }

    private static NamedEntityRecognizer getSpanishStanfordNER() {
        final CRFClassifier<CoreLabel> spanishClassifier = getCRFClassifier("/spanish.ancora.distsim.s512.crf.ser.gz");
        return new StanfordCoreRecognizer(spanishClassifier);
    }

    private static NamedEntityRecognizer getEmailNER() {
        final String emailPattern = "[a-zA-Z0-9'._%+-]+@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,4}";
        return new RegexRecognizer(Pattern.compile(emailPattern), NERType.EMAIL);
    }

    private static NamedEntityRecognizer getURLNER() {
        final String urlPattern = "\\\\b(https?|ftp|file|ldap)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]*[-AZa-z0-9+&@#/%=~_|]";
        return new RegexRecognizer(Pattern.compile(urlPattern), NERType.URL);
    }

    private static NamedEntityRecognizer getOpenNLPEnglishPersonNER() {
        return new OpenNLPRecognizer(getNameFinder("/en-ner-person.bin"), getTokenizer("/en-token.bin"), NERType.PERSON);
    }

    private static NamedEntityRecognizer getOpenNLPEnglishDateNER() {
        return new OpenNLPRecognizer(getNameFinder("/en-ner-date.bin"), getTokenizer("/en-token.bin"), NERType.DATE);
    }

    private static NamedEntityRecognizer getOpenNLPEnglishLocationNER() {
        return new OpenNLPRecognizer(getNameFinder("/en-ner-location.bin"), getTokenizer("/en-token.bin"), NERType.LOCATION);
    }

    private static NamedEntityRecognizer getOpenNLPEnglishOrganizationNER() {
        return new OpenNLPRecognizer(getNameFinder("/en-ner-organization.bin"), getTokenizer("/en-token.bin"), NERType.ORGANIZATION);
    }

    private static NamedEntityRecognizer getOpenNLPEnglishMoneyNER() {
        return new OpenNLPRecognizer(getNameFinder("/en-ner-money.bin"), getTokenizer("/en-token.bin"), NERType.MONEY);
    }

    private static NamedEntityRecognizer getOpenNLPDutchPersonNER() {
        return new OpenNLPRecognizer(getNameFinder("/nl-ner-person.bin"), getTokenizer("/nl-token.bin"), NERType.PERSON);
    }

    private static NamedEntityRecognizer getOpenNLPDutchLocationNER() {
        return new OpenNLPRecognizer(getNameFinder("/nl-ner-location.bin"), getTokenizer("/nl-token.bin"), NERType.LOCATION);
    }

    private static NamedEntityRecognizer getOpenNLPDutchOrganizationNER() {
        return new OpenNLPRecognizer(getNameFinder("/nl-ner-organization.bin"), getTokenizer("/nl-token.bin"), NERType.ORGANIZATION);
    }

    private static NamedEntityRecognizer getOpenNLPSpanishOrganizationNER() {
        return new OpenNLPRecognizer(getNameFinder("/es-ner-organization.bin"), getTokenizerDecorator(), NERType.ORGANIZATION);
    }

    private static NamedEntityRecognizer getOpenNLPSpanishLocationNER() {
        return new OpenNLPRecognizer(getNameFinder("/es-ner-location.bin"), getTokenizerDecorator(), NERType.LOCATION);
    }

    private static NamedEntityRecognizer getOpenNLPSpanishPersonNER() {
        return new OpenNLPRecognizer(getNameFinder("/es-ner-person.bin"), getTokenizerDecorator(), NERType.PERSON);
    }
}
