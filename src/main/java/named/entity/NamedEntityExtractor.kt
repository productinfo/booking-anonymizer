package named.entity

import java.util.*

interface NamedEntityExtractor {

    fun getNamedEntitiesAndPreprocessedText(text: String): HashMap<String, NERType>
}
