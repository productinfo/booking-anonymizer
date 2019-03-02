package named.entity.recognition

import named.entity.NERType
import java.util.*
import java.util.regex.Pattern

class RegexRecognizer(
    private val pattern: Pattern,
    private val nerType: NERType
) : NamedEntityRecognizer() {

    override fun getNamedEntities(text: String): HashMap<String, NERType> {
        Objects.requireNonNull(text)
        val matcher = pattern.matcher(text)
        val namedEntities = HashMap<String, NERType>()
        while (matcher.find()) {
            val namedEntity = matcher.group().toLowerCase()
            namedEntities[namedEntity] = nerType
        }
        return namedEntities
    }
}
