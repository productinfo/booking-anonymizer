package named.entity.proprietoryModels;

import com.aliasi.dict.Dictionary;
import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.ExactDictionaryChunker;
import com.aliasi.dict.MapDictionary;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.opencsv.CSVReader;
import preprocessing.Preprocessor;
import preprocessing.PreprocessorImp;

import java.io.FileReader;

public class LingPipeExactDictionaryChunker {

    public static ExactDictionaryChunker initializeDictionary(String fileName, String nerCategory) {
        final Dictionary<String> dictionary = getProfileDictionary(fileName, nerCategory);
        return new ExactDictionaryChunker(dictionary, IndoEuropeanTokenizerFactory.INSTANCE, true, false);
    }


    private static Dictionary<String> getProfileDictionary(String fileName, String nerCategory) {
        final MapDictionary<String> dictionary = new MapDictionary<>();
        try {
            FileReader filereader = new FileReader(LingPipeExactDictionaryChunker.class.getResource(fileName).getFile());
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;
            Preprocessor preprocessor = new PreprocessorImp();
            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    cell = preprocessor.getPreprocessedText(cell);
                    //profiles should not look like urls
                    cell = cell.replace("www.", "");
                    cell = cell.replace(".de", "");
                    cell = cell.replace(".com", "");
                    dictionary.addEntry(new DictionaryEntry<>(cell, nerCategory, 1.0));
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return dictionary;
    }
}
