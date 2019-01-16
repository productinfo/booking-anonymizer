package constant.sepa;

public enum RelevantSEPAWord {

    SEPA("SVWZ"),
    ABWEICHENDER_AUFTRAGGEBER("ABWA"),
    ERSTER_EINZUG("FRST"),
    WIEDERKEHREND("RCUR"),
    KUNDENREFERENZ("KREF"),
    CUSTOMERREFERENZ("CREF"),
    ZINSKOMPENSATIONSBETRAG("COAM"),
    URSPRUENGLICHER_UMSATZBETRAG("OAMT"),
    ABWEICHENDER_EMPFAENGER("ABWE"),
    RETOURENREFERENZ("RREF");



    private final String SEPAWord;

    RelevantSEPAWord(String SEPAWord) {
        this.SEPAWord = SEPAWord;
    }

    public String getSEPAWord() {
        return SEPAWord;
    }
}