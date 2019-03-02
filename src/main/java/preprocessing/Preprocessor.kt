package preprocessing

interface Preprocessor {

    fun getPreprocessedText(rawText: String): String
}
