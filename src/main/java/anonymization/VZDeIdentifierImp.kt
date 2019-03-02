package anonymization

import named.entity.NERType
import named.entity.NamedEntityExtractor
import java.util.regex.Pattern

open class VZDeIdentifierImp(private val namedEntityExtractor: NamedEntityExtractor?) :
    DeIdentifier {

    open val sensibleNerTypes = hashSetOf(
        NERType.PERSON,
        NERType.MONEY,
        NERType.DATE,
        NERType.MISC,
        NERType.URL,
        NERType.LOCATION,
        NERType.EMAIL
    )

    override fun getDeIdentifiedText(text: String?): String {
        if (text.isNullOrBlank()) {
            return ""
        }

        val namedEntities = namedEntityExtractor?.getNamedEntitiesAndPreprocessedText(text).orEmpty()
        var preprocessedText = getTextWithoutDigits(text)
        for ((entity, entityType) in namedEntities) {
            preprocessedText = getTextWithDeIdentifiedEntity(preprocessedText, entity, entityType)
        }

        return preprocessedText.replace("\\s+".toRegex(), " ").trim()
    }

    protected open fun getTextWithDeIdentifiedEntity(text: String, entity: String, nerType: NERType): String {
        val searchPattern = "(?i)" + Pattern.quote(entity)
        return if (!sensibleNerTypes.contains(nerType)) {
            text
        } else text.replace(searchPattern.toRegex(), "#")
    }

    private fun getTextWithoutDigits(rawText: String): String {
        return rawText.replace("\\p{N}".toRegex(), "#")
    }
}
