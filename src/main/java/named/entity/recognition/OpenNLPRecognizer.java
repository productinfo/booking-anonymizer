package named.entity.recognition;

import named.entity.NERType;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

import java.util.*;

public class OpenNLPRecognizer extends NamedEntityRecognizer {

    private final NameFinderME nameFinder;
    private final Tokenizer tokenizer;
    private final NERType nerType;

    public OpenNLPRecognizer(final NameFinderME nameFinder, final Tokenizer tokenizer, NERType nerType) {
        this.nameFinder = Objects.requireNonNull(nameFinder, "Name Finder cannot be null");
        this.tokenizer = Objects.requireNonNull(tokenizer, "Tokenizer cannot be null");
        this.nerType = Objects.requireNonNull(nerType, "NER Type cannot be null");
    }

    @Override
    public HashMap<String, NERType> getNamedEntities(final String text) {
        Objects.requireNonNull(text, "text cannot be null");
        final String[] tokens = tokenizer.tokenize(text);
        final Span[] nameSpans = nameFinder.find(tokens);
        final HashMap<String, NERType> namedEntities = new HashMap<>();
        for (Span span: nameSpans) {
            String namedEntity = tokens[span.getStart()].toLowerCase();
            namedEntities.put(namedEntity, nerType);
        }
        return namedEntities;
    }
}
