package named.entity.recognition;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class OpenNLPRecognizer implements NamedEntityRecognizer {

    private final NameFinderME nameFinder;
    private final Tokenizer tokenizer;

    public OpenNLPRecognizer(NameFinderME nameFinder, Tokenizer tokenizer) {
        this.nameFinder = Objects.requireNonNull(nameFinder, "Name Finder cannot be null");
        this.tokenizer = Objects.requireNonNull(tokenizer, "Tokenizer cannot be null");
    }

    @Override
    public List<String> getNamedEntities(String text) {
        Objects.requireNonNull(text, "The text cannot be null");
        String[] tokens = tokenizer.tokenize(text);
        Span[] nameSpans = nameFinder.find(tokens);
        List<String> entities = new LinkedList<>();
        for (Span span : nameSpans) {
            entities.add(text.substring(span.getStart(), span.getEnd()));
        }
        return entities;
    }
}
