package anonymization

interface DeIdentifier {
    fun getDeIdentifiedText(text: String?): String
}
