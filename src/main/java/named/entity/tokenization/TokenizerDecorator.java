package named.entity.tokenization;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

public class TokenizerDecorator extends TokenizerME {

    public TokenizerDecorator(TokenizerModel model) {
        super(model);
    }

    @Override
    public String[] tokenize(String s) {
        return WhitespaceTokenizer.INSTANCE.tokenize(s);
    }
}
