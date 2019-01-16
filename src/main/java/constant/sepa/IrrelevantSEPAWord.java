package constant.sepa;

public enum IrrelevantSEPAWord {

    END_TO_END_REFERENCE_ABBREVIATED("eref"),
    END_TO_END_REFERENCE_FULL("endtoendref"),
    MANDATS_REFERENCE_ABBREVIATED("mref"),
    MANDATS_REFERENCE_FULL("mandatsref"),
    CREDITOR("cred");

    private final String SEPAWord;
    IrrelevantSEPAWord(String SEPAWord) {
        this.SEPAWord = SEPAWord;
    }

    public String getSEPAWord() {
        return SEPAWord;
    }
}