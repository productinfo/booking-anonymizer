package part.of.speech.tagging;

import preprocessing.Preprocessor;

public class POSTaggerFactory{

    private static POSTagger namedEntityRecognizer = null;

    public static POSTagger getPOSTagger(Preprocessor preprocessor){
        if(namedEntityRecognizer == null){
            namedEntityRecognizer = new PosTaggerImp(preprocessor);
        }
        return namedEntityRecognizer;
    }
}
