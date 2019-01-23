package initialization;

import anonymization.DeIdentifier;
import anonymization.DeIdentifierImp;
import named.entity.recognition.NamedEntityRecognizer;
import named.entity.recognition.NamedEntityRecognizerImp;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import part.of.speech.tagging.POfSTagger;
import part.of.speech.tagging.POfSTaggerImp;
import preprocessing.Preprocessor;
import preprocessing.PreprocessorImp;

import java.io.IOException;
import java.io.InputStream;

public class DependencyBinder {

    public static DeIdentifier getAnonymizer() {
        final Preprocessor preprocessor = PreprocessorImp.getPreprocessor();
        final POSModel posModel = getPOSModel();
        final POSTaggerME posTaggerME = getPOSTaggerME(posModel);
        final POfSTagger POfSTagger = POfSTaggerImp.getPOfSTagger(preprocessor, posTaggerME);
        final NamedEntityRecognizer namedEntityRecognizer = NamedEntityRecognizerImp.getNamedEntityRecognizer(POfSTagger);
        return DeIdentifierImp.getDeIdentifier(namedEntityRecognizer);
    }

    private static POSModel getPOSModel() {
        final InputStream modelInputStream = DependencyBinder.class.getResourceAsStream("/de-pos-maxent.bin");
        try {
            return new POSModel(modelInputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load Part Of Speech (POS) Model", e);
        }
    }

    private static POSTaggerME getPOSTaggerME(POSModel model) {
        return new POSTaggerME(model);
    }
}
