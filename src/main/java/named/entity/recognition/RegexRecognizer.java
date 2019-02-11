package named.entity.recognition;

import named.entity.NERType;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexRecognizer extends NamedEntityRecognizer {

    private final Pattern pattern;
    private final NERType nerType;

    public RegexRecognizer(final Pattern pattern, final NERType nerType){
        this.pattern = Objects.requireNonNull(pattern, "pattern cannot be null");
        this.nerType = Objects.requireNonNull(nerType, "nerType cannot be null");
    }
    @Override
    public HashMap<String, NERType> getNamedEntities(final String text) {
        Objects.requireNonNull(text);
        final Matcher matcher = pattern.matcher(text);
        final HashMap<String, NERType> namedEntities = new HashMap<>();
        while (matcher.find()){
            String namedEntity = matcher.group().toLowerCase();
            namedEntities.put(namedEntity, nerType);
        }
        return namedEntities;
    }
}
