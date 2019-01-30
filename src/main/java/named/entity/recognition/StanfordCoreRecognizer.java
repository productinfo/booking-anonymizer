package named.entity.recognition;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class StanfordCoreRecognizer implements NamedEntityRecognizer {

    private final CRFClassifier<CoreLabel> classifier;
    private final static String PERSON_CATEGORY = "PERSON";

    public StanfordCoreRecognizer(final CRFClassifier<CoreLabel> classifier) {
        this.classifier = Objects.requireNonNull(classifier, "Classifier cannot be null");
    }

    public List<String> getNamedEntities(String text) {
        Objects.requireNonNull(text, "Raw text text cannot be null");
        final List<List<CoreLabel>> entitySet = classifier.classify(text);
        List<String> entities = new LinkedList<>();
        for (List<CoreLabel> internalList : entitySet) {
            addPersonEntitiesToEntityCollection(internalList, entities);
        }
        return entities;
    }

    private void addPersonEntitiesToEntityCollection(List<CoreLabel> coreLabelList, List<String> entities) {
        for (CoreLabel coreLabel : coreLabelList) {
            String category = coreLabel.get(CoreAnnotations.AnswerAnnotation.class);
            if (PERSON_CATEGORY.equals(category)) {
                entities.add(coreLabel.word());
            }
        }
    }
}
