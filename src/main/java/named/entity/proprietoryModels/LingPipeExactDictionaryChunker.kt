package named.entity.proprietoryModels

import com.aliasi.dict.Dictionary
import com.aliasi.dict.DictionaryEntry
import com.aliasi.dict.ExactDictionaryChunker
import com.aliasi.dict.MapDictionary
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory
import com.opencsv.CSVReader
import preprocessing.PreprocessorImp
import java.io.FileReader

object LingPipeExactDictionaryChunker {

    fun initializeDictionary(fileName: String, nerCategory: String): ExactDictionaryChunker {
        val dictionary = getProfileDictionary(fileName, nerCategory)
        return ExactDictionaryChunker(dictionary, IndoEuropeanTokenizerFactory.INSTANCE, true, false)
    }


    private fun getProfileDictionary(fileName: String, nerCategory: String): Dictionary<String> {
        val dictionary = MapDictionary<String>()
        try {
            val filereader = FileReader(LingPipeExactDictionaryChunker::class.java.getResource(fileName).file)
            val csvReader = CSVReader(filereader)
            val preprocessor = PreprocessorImp()
            var nextRecord: Array<String>
            while (csvReader.readNext() != null) {
                nextRecord = csvReader.readNext()
                for (cell in nextRecord) {
                    val processedCell =
                        preprocessor.getPreprocessedText(cell)
                            .replace("www.", "")
                            .replace(".de", "")
                            .replace(".com", "")
                    dictionary.addEntry(DictionaryEntry(processedCell, nerCategory, 1.0))
                }
            }
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }

        return dictionary
    }
}
