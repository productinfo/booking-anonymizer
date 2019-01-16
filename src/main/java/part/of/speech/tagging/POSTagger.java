package part.of.speech.tagging;

import java.util.List;

public interface POSTagger {

    List<String> removeIrrelevantPartsOfSpeech(List<String> rawText);
}
