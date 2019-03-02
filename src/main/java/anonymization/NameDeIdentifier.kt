package anonymization

import named.entity.NERType
import named.entity.NamedEntityExtractor
import java.util.*
import java.util.regex.Pattern

data class NameDeIdentifier(private val namedEntityExtractor: NamedEntityExtractor?) :
    VZDeIdentifierImp(namedEntityExtractor) {

    override val sensibleNerTypes = hashSetOf(
        NERType.PERSON
    )

    override fun getTextWithDeIdentifiedEntity(text: String, entity: String, nerType: NERType): String {
        val searchPattern = "(?i)" + Pattern.quote(entity)
        return if (!sensibleNerTypes.contains(nerType)) {
            text
        } else text.replace(searchPattern.toRegex(), "#")
    }
}
