package anonymization

import named.entity.NERType
import named.entity.NamedEntityExtractor
import org.apache.commons.lang3.StringUtils
import java.util.*
import java.util.regex.Pattern

open class VZDeIdentifierImp(private val namedEntityExtractor: NamedEntityExtractor?) : DeIdentifier {
    private val sensibleNerTypes: HashSet<NERType> = hashSetOf(
        NERType.PERSON,
        NERType.MONEY,
        NERType.DATE,
        NERType.MISC,
        NERType.URL,
        NERType.LOCATION,
        NERType.EMAIL)

    open val sensibleNERTypes: HashSet<NERType>
        get() {
            return hashSetOf(
                NERType.PERSON,
                NERType.MONEY,
                NERType.DATE,
                NERType.MISC,
                NERType.URL,
                NERType.LOCATION,
                NERType.EMAIL
            )
        }


    override fun getDeIdentifiedText(text: String?): String {
        if (text.isNullOrBlank()) {
            return ""
        }
        val namedEntities = namedEntityExtractor?.getNamedEntitiesAndPreprocessedText(text).orEmpty()
        var preprocessedText = getTextWithoutDigits(text)
        for ((entity, entityType) in namedEntities) {
            preprocessedText = getTextWithDeIdentifiedEntity(preprocessedText, entity, entityType)
        }
        return StringUtils.normalizeSpace(preprocessedText)
    }

    protected open fun getTextWithDeIdentifiedEntity(text: String, entity: String, nerType: NERType): String {

        if (!sensibleNerTypes.contains(nerType)) {
            return text
        }
        val searchPattern = "(?i)" + Pattern.quote(entity)
        return text.replace(searchPattern.toRegex(), "#")
    }

    private fun getTextWithoutDigits(rawText: String): String {
        return rawText.replace("\\p{N}".toRegex(), "#")
    }
}
