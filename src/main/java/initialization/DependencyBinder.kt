package initialization

import anonymization.*
import edu.stanford.nlp.ie.crf.CRFClassifier
import edu.stanford.nlp.ling.CoreLabel
import named.entity.NERType
import named.entity.NamedEntityExtractorImpl
import named.entity.proprietoryModels.LingPipeExactDictionaryChunker
import named.entity.recognition.*
import named.entity.tokenization.TokenizerDecorator
import opennlp.tools.namefind.NameFinderME
import opennlp.tools.namefind.TokenNameFinderModel
import opennlp.tools.tokenize.Tokenizer
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import preprocessing.PreprocessorImp
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

object DependencyBinder {

    private const val INITIAL_CAPACITY = 33
    private val NAMED_ENTITY_RECOGNIZERS: List<NamedEntityRecognizer>

    init {
        NAMED_ENTITY_RECOGNIZERS = namedEntityRecognizers
    }

    val vzDeIdentifier: DeIdentifier
        get() {
            val namedEntityExtractor = NamedEntityExtractorImpl(PreprocessorImp(), NAMED_ENTITY_RECOGNIZERS)
            return VZDeIdentifierImpl(
                EntityDeIdentifierDelegate(
                    namedEntityExtractor, hashSetOf(
                        NERType.PERSON,
                        NERType.MONEY,
                        NERType.DATE,
                        NERType.MISC,
                        NERType.URL,
                        NERType.LOCATION,
                        NERType.EMAIL
                    )
                )
            )
        }

    val nameDeIdentifier: DeIdentifier
        get() {
            val namedEntityExtractor = NamedEntityExtractorImpl(PreprocessorImp(), NAMED_ENTITY_RECOGNIZERS)
            return NameDeIdentifier(
                EntityDeIdentifierDelegate(
                    namedEntityExtractor, hashSetOf(
                        NERType.PERSON
                    )
                )
            )
        }

    val amountDeIdentifier: DeIdentifier
        get() {
            val namedEntityExtractor = NamedEntityExtractorImpl(PreprocessorImp(), NAMED_ENTITY_RECOGNIZERS)
            return AmountDeIdentifierImpl(
                EntityDeIdentifierDelegate(namedEntityExtractor)
            )
        }


    val namedEntityRecognizers: List<NamedEntityRecognizer>
        get() {
            val namedEntityRecognizers = ArrayList<NamedEntityRecognizer>(INITIAL_CAPACITY)
            namedEntityRecognizers.add(0, germanFirmProprietoryModel)
            namedEntityRecognizers.add(1, openNLPEnglishOrganizationNER)
            namedEntityRecognizers.add(2, openNLPDutchOrganizationNER)
            namedEntityRecognizers.add(3, openNLPSpanishOrganizationNER)
            namedEntityRecognizers.add(4, germanCityProprietoryModel)
            namedEntityRecognizers.add(5, openNLPEnglishMoneyNER)
            namedEntityRecognizers.add(6, openNLPDutchLocationNER)
            namedEntityRecognizers.add(7, openNLPEnglishLocationNER)
            namedEntityRecognizers.add(8, openNLPSpanishLocationNER)
            namedEntityRecognizers.add(9, germanStanfordNER)
            namedEntityRecognizers.add(10, englishStanfordNER)
            namedEntityRecognizers.add(11, spanishStanfordNER)
            namedEntityRecognizers.add(12, openNLPDutchPersonNER)
            namedEntityRecognizers.add(13, openNLPEnglishPersonNER)
            namedEntityRecognizers.add(14, openNLPSpanishPersonNER)
            namedEntityRecognizers.add(15, openNLPEnglishDateNER)
            namedEntityRecognizers.add(16, urlner)
            namedEntityRecognizers.add(17, emailNER)
            return namedEntityRecognizers
        }


    private val tokenizerDecorator: Tokenizer
        get() = TokenizerDecorator(getTokenizerModel("/en-token.bin"))

    private val englishStanfordNER: NamedEntityRecognizer
        get() {
            val englishClassifier = getCRFClassifier("/english.all.3class.caseless.distsim.crf.ser.gz")
            return StanfordCoreRecognizer(englishClassifier)
        }

    private val germanStanfordNER: NamedEntityRecognizer
        get() {
            val germanClassifier = getCRFClassifier("/german.conll.germeval2014.hgc_175m_600.crf.ser.gz")
            return StanfordCoreRecognizer(germanClassifier)
        }

    private val spanishStanfordNER: NamedEntityRecognizer
        get() {
            val spanishClassifier = getCRFClassifier("/spanish.ancora.distsim.s512.crf.ser.gz")
            return StanfordCoreRecognizer(spanishClassifier)
        }

    private val emailNER: NamedEntityRecognizer
        get() {
            val emailPattern = "[a-zA-Z0-9'._%+-]+@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,4}"
            return RegexRecognizer(Pattern.compile(emailPattern), NERType.EMAIL)
        }

    private val urlner: NamedEntityRecognizer
        get() {
            val urlPattern = "\\\\b(https?|ftp|file|ldap)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]*[-AZa-z0-9+&@#/%=~_|]"
            return RegexRecognizer(Pattern.compile(urlPattern), NERType.URL)
        }

    private val openNLPEnglishPersonNER: NamedEntityRecognizer
        get() = OpenNLPRecognizer(getNameFinder("/en-ner-person.bin"), getTokenizer("/en-token.bin"), NERType.PERSON)

    private val openNLPEnglishDateNER: NamedEntityRecognizer
        get() = OpenNLPRecognizer(getNameFinder("/en-ner-date.bin"), getTokenizer("/en-token.bin"), NERType.DATE)

    private val openNLPEnglishLocationNER: NamedEntityRecognizer
        get() = OpenNLPRecognizer(
            getNameFinder("/en-ner-location.bin"),
            getTokenizer("/en-token.bin"),
            NERType.LOCATION
        )

    private val openNLPEnglishOrganizationNER: NamedEntityRecognizer
        get() = OpenNLPRecognizer(
            getNameFinder("/en-ner-organization.bin"),
            getTokenizer("/en-token.bin"),
            NERType.ORGANIZATION
        )

    private val openNLPEnglishMoneyNER: NamedEntityRecognizer
        get() = OpenNLPRecognizer(getNameFinder("/en-ner-money.bin"), getTokenizer("/en-token.bin"), NERType.MONEY)

    private val openNLPDutchPersonNER: NamedEntityRecognizer
        get() = OpenNLPRecognizer(getNameFinder("/nl-ner-person.bin"), getTokenizer("/nl-token.bin"), NERType.PERSON)

    private val openNLPDutchLocationNER: NamedEntityRecognizer
        get() = OpenNLPRecognizer(
            getNameFinder("/nl-ner-location.bin"),
            getTokenizer("/nl-token.bin"),
            NERType.LOCATION
        )

    private val openNLPDutchOrganizationNER: NamedEntityRecognizer
        get() = OpenNLPRecognizer(
            getNameFinder("/nl-ner-organization.bin"),
            getTokenizer("/nl-token.bin"),
            NERType.ORGANIZATION
        )

    private val openNLPSpanishOrganizationNER: NamedEntityRecognizer
        get() = OpenNLPRecognizer(getNameFinder("/es-ner-organization.bin"), tokenizerDecorator, NERType.ORGANIZATION)

    private val openNLPSpanishLocationNER: NamedEntityRecognizer
        get() = OpenNLPRecognizer(getNameFinder("/es-ner-location.bin"), tokenizerDecorator, NERType.LOCATION)

    private val openNLPSpanishPersonNER: NamedEntityRecognizer
        get() = OpenNLPRecognizer(getNameFinder("/es-ner-person.bin"), tokenizerDecorator, NERType.PERSON)

    private val germanCityProprietoryModel: NamedEntityRecognizer
        get() = LingPipeDictionaryRecognizer(
            LingPipeExactDictionaryChunker.initializeDictionary(
                "/staedte.csv",
                "LOCATION"
            ), NERType.LOCATION
        )

    private val germanFirmProprietoryModel: NamedEntityRecognizer
        get() = LingPipeDictionaryRecognizer(
            LingPipeExactDictionaryChunker.initializeDictionary(
                "/firmennamen.csv",
                "ORGANIZATION"
            ), NERType.ORGANIZATION
        )

    private fun getCRFClassifier(modelName: String): CRFClassifier<CoreLabel> {
        val loadPath = DependencyBinder::class.java.getResource(modelName).path
        return CRFClassifier.getClassifierNoExceptions(loadPath)
    }

    private fun getNameFinder(nameFinderModelName: String): NameFinderME {
        val modelInputStream = DependencyBinder::class.java.getResourceAsStream(nameFinderModelName)
        try {
            val tokenNameFinderModel = TokenNameFinderModel(modelInputStream)
            return NameFinderME(tokenNameFinderModel)
        } catch (e: IOException) {
            throw IllegalStateException("Unable to load Name Finder Moder Model", e)
        }
    }

    private fun getTokenizerModel(tokenizerModelName: String): TokenizerModel {
        val modelInputStream = DependencyBinder::class.java.getResourceAsStream(tokenizerModelName)
        try {
            return TokenizerModel(modelInputStream)
        } catch (e: IOException) {
            throw IllegalStateException("Unable to load Tokenizer Model", e)
        }
    }

    private fun getTokenizer(tokenizerModelName: String): Tokenizer {
        return TokenizerME(getTokenizerModel(tokenizerModelName))
    }
}
