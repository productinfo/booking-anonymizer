package preprocessing

class PreprocessorImp : Preprocessor {

    override fun getPreprocessedText(rawText: String): String {
        val preprocessedText = rawText.replace("\\b(?i)EC\\b".toRegex(), " ")
        return preprocessedText.replace("\\s+".toRegex(), " ").trim()
    }
}
