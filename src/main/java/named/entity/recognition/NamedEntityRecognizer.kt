package named.entity.recognition

import named.entity.NERType
import java.util.*

abstract class NamedEntityRecognizer {

    abstract fun getNamedEntities(text: String): HashMap<String, NERType>

    companion object {

        val NER_TYPES = mutableMapOf<String, NERType>()

        init {
            NER_TYPES["PERSON"] = NERType.PERSON
            NER_TYPES["LOCATION"] = NERType.LOCATION
            NER_TYPES["ORGANIZATION"] = NERType.ORGANIZATION
            NER_TYPES["MONEY"] = NERType.MONEY
            NER_TYPES["DATE"] = NERType.DATE
        }
    }
}
