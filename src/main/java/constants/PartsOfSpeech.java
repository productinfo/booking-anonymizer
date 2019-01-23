package constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PartsOfSpeech {

    /**
     * German Parts Of Speech Symbols https://www.sketchengine.eu/german-stts-part-of-speech-tagset/
     */
    public final static Set<String> UNWANTED_PARTS_OF_SPEECH = new HashSet<>(Arrays.asList(
            "ADJA", "ADJD", "ADV", "APPR", "APPRART", "APPO",
            "APZR", "ART", "KON", "KOKOM", "KOUI", "KOUS",
            "PAV", "PROAV", "PAVREL", "PDAT", "PDS", "PIAT",
            "PIS", "PPER", "PRF", "PPOSS", "PPOSAT", "PRELAT",
            "PRELS", "PTKA", "PTKANT", "PTKNEG", "PTKREL", "PTKVZ",
            "PTKZU", "PWS", "PWAT", "PWAV", "PWAVREL", "PWREL", "TRUNC"));
}
