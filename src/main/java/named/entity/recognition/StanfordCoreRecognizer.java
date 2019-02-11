package named.entity.recognition;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import named.entity.NERType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class StanfordCoreRecognizer extends NamedEntityRecognizer {

    private final CRFClassifier<CoreLabel> classifier;

    public StanfordCoreRecognizer(final CRFClassifier<CoreLabel> classifier) {
        this.classifier = Objects.requireNonNull(classifier, "Classifier cannot be null");
    }

    public HashMap<String, NERType> getNamedEntities(final String text) {
        Objects.requireNonNull(text, "Raw text text cannot be null");
        final List<List<CoreLabel>> entitySet = classifier.classify(text);
        final HashMap<String, NERType> namedEntities = new HashMap<>();
        for (List<CoreLabel> internalList : entitySet) {
            addPersonEntitiesToEntityCollection(internalList, namedEntities);
        }
        return namedEntities;
    }

    private void addPersonEntitiesToEntityCollection(List<CoreLabel> coreLabelList, HashMap<String, NERType> namedEntities) {
        for (CoreLabel coreLabel : coreLabelList) {
            String namedEntityType = coreLabel.get(CoreAnnotations.AnswerAnnotation.class).toUpperCase();
            if(NER_TYPES.containsKey(namedEntityType)){
                String namedEntity = coreLabel.word().toLowerCase();
                namedEntities.put(namedEntity, NER_TYPES.get(namedEntityType));
            }
        }
    }
}
