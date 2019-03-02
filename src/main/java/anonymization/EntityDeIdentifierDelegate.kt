package anonymization

import named.entity.NERType
import named.entity.NamedEntityExtractor
import java.util.regex.Pattern

data class EntityDeIdentifierDelegate(
    private val namedEntityExtractor: NamedEntityExtractor?,
    override val sensibleNerTypes: HashSet<NERType> = hashSetOf()
) : DeIdentifier {

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

    private fun getTextWithDeIdentifiedEntity(text: String, entity: String, nerType: NERType): String {
        val searchPattern = "(?i)" + Pattern.quote(entity)
        return if (!sensibleNerTypes.contains(nerType)) {
            text
        } else text.replace(searchPattern.toRegex(), "#")
    }

    private fun getTextWithoutDigits(rawText: String): String {
        return rawText.replace("\\p{N}".toRegex(), "#")
    }
}