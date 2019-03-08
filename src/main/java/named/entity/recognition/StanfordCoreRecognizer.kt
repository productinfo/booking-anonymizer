package named.entity.recognition

import edu.stanford.nlp.ie.crf.CRFClassifier
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.ling.CoreLabel
import named.entity.NERType
import java.util.*

data class StanfordCoreRecognizer(private val classifier: CRFClassifier<CoreLabel>) : NamedEntityRecognizer() {

    override fun getNamedEntities(text: String): HashMap<String, NERType> {
        val entitySet = classifier.classify(text)
        val namedEntities = hashMapOf<String, NERType>()
        for (internalList in entitySet) {
            addPersonEntitiesToEntityCollection(internalList, namedEntities)
        }
        return namedEntities
    }

    private fun addPersonEntitiesToEntityCollection(
        coreLabelList: List<CoreLabel>,
        namedEntities: HashMap<String, NERType>
    ) {
        for (coreLabel in coreLabelList) {
            val namedEntityType = coreLabel.get(CoreAnnotations.AnswerAnnotation::class.java).toUpperCase()
            if (NamedEntityRecognizer.NER_TYPES.containsKey(namedEntityType)) {
                val namedEntity = coreLabel.word().toLowerCase()
                NamedEntityRecognizer.NER_TYPES[namedEntityType]?.let { namedEntities[namedEntity] = it }
            }
        }
    }
}
