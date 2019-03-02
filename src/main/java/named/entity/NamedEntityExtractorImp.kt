package named.entity

import named.entity.recognition.NamedEntityRecognizer
import preprocessing.Preprocessor
import utility.Constants
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class NamedEntityExtractorImp(
    private val preprocessor: Preprocessor,
    private val namedEntityRecognizers: List<NamedEntityRecognizer>
) : NamedEntityExtractor {

    override fun getNamedEntitiesAndPreprocessedText(text: String): HashMap<String, NERType> {
        val tempNamedEntities = ConcurrentHashMap<String, NERType>()
        val preprocessedText = preprocessor.getPreprocessedText(text)

        // TODO migrate to Coroutines
        val numberOfThreads = Constants.NUMBER_OF_AVAILABLE_CORES
        val executor = Executors.newFixedThreadPool(numberOfThreads)
        for (i in 0 until numberOfThreads) {
            val quizMakerWorker = NERRunnable(preprocessedText, tempNamedEntities, i, numberOfThreads)
            executor.execute(quizMakerWorker)
        }
        awaitTerminationOfThreads(executor)
        return HashMap(tempNamedEntities)
    }

    private fun awaitTerminationOfThreads(executor: ExecutorService) {
        executor.shutdown()
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            throw IllegalStateException("There was an error when terminating threads", e)
        }

    }

    private inner class NERRunnable(
        private val text: String,
        private val namedEntities: ConcurrentHashMap<String, NERType>,
        private val threadNumber: Int,
        private val threadCount: Int
    ) : Runnable {

        override fun run() {
            val numberOfNERs = namedEntityRecognizers.size
            var i = threadNumber
            while (i < numberOfNERs) {
                val foundEntities = namedEntityRecognizers[i].getNamedEntities(text)
                addFoundEntitiesToMap(foundEntities)
                i += threadCount
            }
        }

        private fun addFoundEntitiesToMap(foundEntities: HashMap<String, NERType>) {
            for ((entity, entityType) in foundEntities) {
                namedEntities.putIfAbsent(entity, entityType)
            }
        }
    }
}
