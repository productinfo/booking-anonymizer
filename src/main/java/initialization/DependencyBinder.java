package initialization;

import anonymization.AmountDeIdentifier;
import anonymization.DeIdentifier;
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
import preprocessing.Preprocessor;
import preprocessing.PreprocessorImp;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class DependencyBinder {

    private static final List<NamedEntityRecognizer> NAMED_ENTITY_RECOGNIZERS;

    static {
        NAMED_ENTITY_RECOGNIZERS = getNamedEntityRecognizers();
    }

    public static DeIdentifier getVZDeIdentifier() {
        final Preprocessor preprocessor = new PreprocessorImp();
        final NamedEntityExtractor namedEntityExtractor = new NamedEntityExtractorImp(preprocessor, NAMED_ENTITY_RECOGNIZERS);
        return new VZDeIdentifierImp(namedEntityExtractor);
    }

    public static DeIdentifier getBetragDeIdentifier(){
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
        List<NamedEntityRecognizer> namedEntityRecognizers = new LinkedList<>();

        final String urlPattern = "\\\\b(https?|ftp|file|ldap)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]*[-AZa-z0-9+&@#/%=~_|]";
        final String emailPattern = "[a-zA-Z0-9'._%+-]+@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,4}";

        final NamedEntityRecognizer urlRegexRecognizer = new RegexRecognizer(Pattern.compile(urlPattern), NERType.URL);
        final NamedEntityRecognizer emailRegexRecognizer = new RegexRecognizer(Pattern.compile(emailPattern), NERType.EMAIL);

        final CRFClassifier<CoreLabel> germanClassifier = getCRFClassifier("/german.conll.germeval2014.hgc_175m_600.crf.ser.gz");
        final NamedEntityRecognizer germanStanfordNER = new StanfordCoreRecognizer(germanClassifier);

        final CRFClassifier<CoreLabel> englishClassifier = getCRFClassifier("/english.all.3class.caseless.distsim.crf.ser.gz");
        final NamedEntityRecognizer englishStanfordNER = new StanfordCoreRecognizer(englishClassifier);

        final CRFClassifier<CoreLabel> spanishClassifier = getCRFClassifier("/spanish.ancora.distsim.s512.crf.ser.gz");
        final NamedEntityRecognizer spanishStanfordNER = new StanfordCoreRecognizer(spanishClassifier);

        final NamedEntityRecognizer englishOpenNLPPersonRecognizer = new OpenNLPRecognizer(getNameFinder("/en-ner-person.bin"), getTokenizer("/en-token.bin"), NERType.PERSON);
        final NamedEntityRecognizer englishOpenNLPDateRecognizer = new OpenNLPRecognizer(getNameFinder("/en-ner-date.bin"), getTokenizer("/en-token.bin"), NERType.DATE);
        final NamedEntityRecognizer englishOpenNLPLocationRecognizer = new OpenNLPRecognizer(getNameFinder("/en-ner-location.bin"), getTokenizer("/en-token.bin"), NERType.LOCATION);
        final NamedEntityRecognizer englishOpenNLPMoneyRecognizer = new OpenNLPRecognizer(getNameFinder("/en-ner-money.bin"), getTokenizer("/en-token.bin"), NERType.MONEY);
        final NamedEntityRecognizer englishOpenNLPOrganizationsRecognizer = new OpenNLPRecognizer(getNameFinder("/en-ner-organization.bin"), getTokenizer("/en-token.bin"), NERType.ORGANIZATION);

        final NamedEntityRecognizer spanishOpenNLPPersonRecognizer = new OpenNLPRecognizer(getNameFinder("/es-ner-person.bin"), getTokenizerDecorator(), NERType.PERSON);
        final NamedEntityRecognizer spanishOpenNLPLocationRecognizer = new OpenNLPRecognizer(getNameFinder("/es-ner-location.bin"), getTokenizerDecorator(), NERType.LOCATION);
        final NamedEntityRecognizer spanishOpenNLPOrganizationsRecognizer = new OpenNLPRecognizer(getNameFinder("/es-ner-organization.bin"), getTokenizerDecorator(), NERType.ORGANIZATION);

        final NamedEntityRecognizer dutchOpenNLPPersonRecognizer = new OpenNLPRecognizer(getNameFinder("/nl-ner-person.bin"), getTokenizer("/nl-token.bin"), NERType.PERSON);
        final NamedEntityRecognizer dutchOpenNLPLocationRecognizer = new OpenNLPRecognizer(getNameFinder("/nl-ner-location.bin"), getTokenizer("/nl-token.bin"), NERType.LOCATION);
        final NamedEntityRecognizer dutchOpenNLPOrganizationsRecognizer = new OpenNLPRecognizer(getNameFinder("/nl-ner-organization.bin"), getTokenizer("/nl-token.bin"), NERType.ORGANIZATION);

        // Order Matters to the Implementation.
        // Do not change unless you know what you are doing.
        namedEntityRecognizers.add(englishOpenNLPMoneyRecognizer);
        namedEntityRecognizers.add(dutchOpenNLPLocationRecognizer);
        namedEntityRecognizers.add(englishOpenNLPLocationRecognizer);
        namedEntityRecognizers.add(spanishOpenNLPLocationRecognizer);
        namedEntityRecognizers.add(germanStanfordNER);
        namedEntityRecognizers.add(englishStanfordNER);
        namedEntityRecognizers.add(spanishStanfordNER);
        namedEntityRecognizers.add(dutchOpenNLPPersonRecognizer);
        namedEntityRecognizers.add(englishOpenNLPPersonRecognizer);
        namedEntityRecognizers.add(spanishOpenNLPPersonRecognizer);
        namedEntityRecognizers.add(englishOpenNLPOrganizationsRecognizer);
        namedEntityRecognizers.add(dutchOpenNLPOrganizationsRecognizer);
        namedEntityRecognizers.add(spanishOpenNLPOrganizationsRecognizer);
        namedEntityRecognizers.add(englishOpenNLPDateRecognizer);
        namedEntityRecognizers.add(urlRegexRecognizer);
        namedEntityRecognizers.add(emailRegexRecognizer);
        return namedEntityRecognizers;
    }
}
