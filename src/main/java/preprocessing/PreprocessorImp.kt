package preprocessing

import org.apache.commons.lang3.StringUtils

class PreprocessorImp : Preprocessor {

    override fun getPreprocessedText(rawText: String): String {
        val preprocessedText = rawText.replace("\\b(?i)EC\\b".toRegex(), " ")
        return StringUtils.normalizeSpace(preprocessedText)
    }
}
