package named.entity.recognition;

import part.of.speech.tagging.POSTagger;

public class NamedEntityRecognizerFactory {

    private static NamedEntityRecognizer namedEntityRecognizer = null;

    public static NamedEntityRecognizer getNamedEntityRecognizer(POSTagger posTagger){
        if(namedEntityRecognizer == null){
            namedEntityRecognizer = new NamedEntityRecognizerImp(posTagger);
        }
        return namedEntityRecognizer;
    }
}
