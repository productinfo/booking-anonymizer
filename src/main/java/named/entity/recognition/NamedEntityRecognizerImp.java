package named.entity.recognition;

import part.of.speech.tagging.POSTagger;

import java.util.List;

class NamedEntityRecognizerImp implements NamedEntityRecognizer {

    private final POSTagger posTagger;

    NamedEntityRecognizerImp(POSTagger posTagger){
        this.posTagger = posTagger;
    }

    /**
     * TODO Implement Named Entity Recognizer
     */
    public List<List<String>> getNamedEntitiesFromText(List<String> text) {
        return null;
    }
}
