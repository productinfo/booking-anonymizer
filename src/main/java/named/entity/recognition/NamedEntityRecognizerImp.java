package named.entity.recognition;

import part.of.speech.tagging.POfSTagger;

import java.util.List;

class NamedEntityRecognizerImp implements NamedEntityRecognizer {

    private final POfSTagger pOfSTagger;

    NamedEntityRecognizerImp(POfSTagger pOfSTagger) {
        this.pOfSTagger = pOfSTagger;
    }

    /**
     * TODO Implement Named Entity Recognizer
     */
    public List<List<String>> getNamedEntitiesFromText(List<String> text) {
        return null;
    }
}
