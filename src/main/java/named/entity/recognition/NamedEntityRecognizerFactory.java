package named.entity.recognition;

import part.of.speech.tagging.POfSTagger;

public class NamedEntityRecognizerFactory {

    private static NamedEntityRecognizer namedEntityRecognizer = null;

    public static NamedEntityRecognizer getNamedEntityRecognizer(POfSTagger pOfSTagger) {
        if (namedEntityRecognizer == null) {
            namedEntityRecognizer = new NamedEntityRecognizerImp(pOfSTagger);
        }
        return namedEntityRecognizer;
    }
}
