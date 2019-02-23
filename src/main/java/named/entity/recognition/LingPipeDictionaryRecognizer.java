package named.entity.recognition;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import named.entity.NERType;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class LingPipeDictionaryRecognizer extends NamedEntityRecognizer{

    private final NERType nerType;
    private final Chunker chunker;

    public LingPipeDictionaryRecognizer(final Chunker chunker, final NERType nerType){
        this.chunker = Objects.requireNonNull(chunker, "Chunker cannot be null");
        this.nerType = Objects.requireNonNull(nerType, "NER Type cannot be null");
    }

    @Override
    public HashMap<String, NERType> getNamedEntities(String text) {
        Objects.requireNonNull(text, "text cannot be null");
        final HashMap<String, NERType> namedEntities = new HashMap<>();
        Chunking chunking = chunker.chunk(text);
        Set<Chunk> chunkSet = chunking.chunkSet();
        for (Chunk chunk: chunkSet){
            namedEntities.put(text.substring(chunk.start(), chunk.end()), nerType);
        }
        return namedEntities;
    }
}
