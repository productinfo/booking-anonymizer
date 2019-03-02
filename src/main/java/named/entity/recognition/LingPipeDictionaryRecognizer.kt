package named.entity.recognition

import com.aliasi.chunk.Chunker
import named.entity.NERType
import java.util.*

data class LingPipeDictionaryRecognizer(
    private val chunker: Chunker,
    private val nerType: NERType
) : NamedEntityRecognizer() {

    override fun getNamedEntities(text: String): HashMap<String, NERType> {
        val namedEntities = HashMap<String, NERType>()
        val chunkedText = chunker.chunk(text)
        val chunkSet = chunkedText.chunkSet()
        for (chunk in chunkSet) {
            val key = text.substring(chunk.start(), chunk.end())
            namedEntities[key] = nerType
        }
        return namedEntities
    }
}
