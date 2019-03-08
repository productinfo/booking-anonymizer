package named.entity.recognition

import named.entity.NERType
import opennlp.tools.namefind.NameFinderME
import opennlp.tools.tokenize.Tokenizer
import opennlp.tools.util.Span
import java.util.*

class OpenNLPRecognizer(
    private val nameFinder: NameFinderME,
    private val tokenizer: Tokenizer,
    private val nerType: NERType
) : NamedEntityRecognizer() {

    override fun getNamedEntities(text: String): HashMap<String, NERType> {
        val tokens: Array<out String>
        synchronized(tokenizerLock) {
            tokens = tokenizer.tokenize(text)
        }

        val nameSpans: Array<out Span>
        synchronized(nameFinderLock) {
            nameSpans = nameFinder.find(tokens)
        }

        val namedEntities = HashMap<String, NERType>()
        nameSpans.forEach {
            val namedEntity = tokens[it.start].toLowerCase()
            namedEntities[namedEntity] = nerType
        }

        return namedEntities
    }

    companion object {
        val tokenizerLock = Any()
        val nameFinderLock = Any()
    }
}
