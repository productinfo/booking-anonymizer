package anonymization

import named.entity.NERType

interface DeIdentifier {
    val sensibleNerTypes: HashSet<NERType>
    fun getDeIdentifiedText(text: String?): String
}
