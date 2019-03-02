package named.entity.recognition

import named.entity.NERType
import opennlp.tools.namefind.NameFinderME
import opennlp.tools.tokenize.Tokenizer
import java.util.*

class OpenNLPRecognizer(
    private val nameFinder: NameFinderME,
    private val tokenizer: Tokenizer,
    private val nerType: NERType
) : NamedEntityRecognizer() {

    override fun getNamedEntities(text: String): HashMap<String, NERType> {
        val tokens = tokenizer.tokenize(text)
        val nameSpans = nameFinder.find(tokens)
        val namedEntities = HashMap<String, NERType>()
        for (span in nameSpans) {
            val namedEntity = tokens[span.start].toLowerCase()
            namedEntities[namedEntity] = nerType
        }
        return namedEntities
    }
}
