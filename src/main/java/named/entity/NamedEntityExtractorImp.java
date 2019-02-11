package named.entity;

import named.entity.recognition.NamedEntityRecognizer;
import preprocessing.Preprocessor;
import utility.Constants;

import java.util.*;
import java.util.concurrent.*;

public class NamedEntityExtractorImp implements NamedEntityExtractor {

    private final Preprocessor preprocessor;
    private final List<NamedEntityRecognizer> namedEntityRecognizers;

    public NamedEntityExtractorImp(final Preprocessor preprocessor, List<NamedEntityRecognizer> namedEntityRecognizers) {
        this.namedEntityRecognizers = Objects.requireNonNull(namedEntityRecognizers, "NamedEntityRecognizer list cannot be null");
        this.preprocessor = Objects.requireNonNull(preprocessor, "PreprocessorTagger cannot be null");
    }

    @Override
    public HashMap<String, NERType> getNamedEntitiesAndPreprocessedText(final String text) {
        Objects.requireNonNull(text, "text cannot be null");
        final ConcurrentHashMap<String, NERType> tempNamedEntities = new ConcurrentHashMap<>();
        final String preprocessedText = preprocessor.getPreprocessedText(text);
        final int numberOfThreads = Constants.NUMBER_OF_AVAILABLE_CORES;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            Runnable quizMakerWorker = new NERWorker(preprocessedText, tempNamedEntities, i, numberOfThreads);
            executor.execute(quizMakerWorker);
        }
        awaitTerminationOfThreads(executor);
        return new HashMap<>(tempNamedEntities);
    }

    private void awaitTerminationOfThreads(ExecutorService executor) {
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new IllegalStateException("There was an error when terminating threads", e);
        }
    }

    private class NERWorker implements Runnable {

        private final String text;
        private final ConcurrentHashMap<String, NERType> namedEntities;
        private final int threadNumber;
        private final int threadCount;

        public NERWorker(final String text, final ConcurrentHashMap<String, NERType> namedEntities, final int threadNumber, final int threadCount) {
            this.text = text;
            this.namedEntities = namedEntities;
            this.threadNumber = threadNumber;
            this.threadCount = threadCount;
        }

        @Override
        public void run() {
            int numberOfNERs = namedEntityRecognizers.size();
            for (int i = threadNumber; i < numberOfNERs; i += threadCount) {
                HashMap<String, NERType> foundEntities = namedEntityRecognizers.get(i).getNamedEntities(text);
                addFoundEntitiesToMap(foundEntities);
            }
        }

        private void addFoundEntitiesToMap(HashMap<String, NERType> foundEntities){
            for (Map.Entry<String, NERType> namedEntity : foundEntities.entrySet()) {
                String entity = namedEntity.getKey();
                NERType entityType = namedEntity.getValue();
                namedEntities.putIfAbsent(entity, entityType);
            }
        }
    }
}
