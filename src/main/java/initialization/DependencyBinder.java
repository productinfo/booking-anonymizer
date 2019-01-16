package initialization;

import anonymization.DeIdentifier;
import anonymization.DeIdentifierFactory;
import co.reference.resolution.CoReferenceResolver;
import co.reference.resolution.CoReferenceResolverFactory;
import named.entity.recognition.NamedEntityRecognizer;
import named.entity.recognition.NamedEntityRecognizerFactory;
import part.of.speech.tagging.POSTagger;
import part.of.speech.tagging.POSTaggerFactory;
import preprocessing.Preprocessor;
import preprocessing.PreprocessorFactory;

public class DependencyBinder {

    public static DeIdentifier getAnonymizer(){
        final Preprocessor preprocessor = PreprocessorFactory.getPreprocessor();
        final POSTagger posTagger = POSTaggerFactory.getPOSTagger(preprocessor);
        final NamedEntityRecognizer namedEntityRecognizer = NamedEntityRecognizerFactory.getNamedEntityRecognizer(posTagger);
        final CoReferenceResolver coReferenceResolver = CoReferenceResolverFactory.getCoReferenceResolver(namedEntityRecognizer);
        return DeIdentifierFactory.getDeIdentifier(coReferenceResolver);
    }
}
