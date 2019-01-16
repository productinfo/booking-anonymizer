package part.of.speech.tagging;

import preprocessing.Preprocessor;

import java.util.List;

class PosTaggerImp implements POSTagger{

    private final Preprocessor preprocessor;

    PosTaggerImp(Preprocessor preprocessor){
        this.preprocessor = preprocessor;
    }

    /**
     * TODO Implement POS Tagger
     */
    public List<String> removeIrrelevantPartsOfSpeech(List<String> rawText) {
        return null;
    }
}
