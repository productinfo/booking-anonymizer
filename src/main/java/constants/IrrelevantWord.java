package constants;

public enum IrrelevantWord {

    END_TO_END_REFERENCE_ABBREVIATED("eref"),
    END_TO_END_REFERENCE_FULL("endtoendref"),
    MANDATS_REFERENCE_ABBREVIATED("mref"),
    MANDATS_REFERENCE_FULL("mandatsref"),
    CREDITOR("cred"),
    IBAN("iban"),
    BIC("bic"),
    ORIGINATOR("debt"),
    REF(" ref "), //The Spacing here is intentional
    BANKREFERENZ("bref"),
    UHR("uhr"),
    GLAEUBIGER_ID("Gläubigerid");

    private final String word;

    IrrelevantWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}