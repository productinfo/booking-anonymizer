package constant.sepa;

public enum RelevantSEPAWord {

    SEPA("svwz"),
    ABWEICHENDER_AUFTRAGGEBER("abwa"),
    MANDATS_REFERENCE_ABBREVIATED("mref"),
    ERSTER_EINZUG("FRST"),
    WIEDERKEHREND("RCUR");

    private final String SEPAWord;

    RelevantSEPAWord(String SEPAWord) {
        this.SEPAWord = SEPAWord;
    }

    public String getSEPAWord() {
        return SEPAWord;
    }
}