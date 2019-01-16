package preprocessing;

public class PreprocessorFactory {

    private static Preprocessor preprocessor = null;

    public static Preprocessor getPreprocessor(){
        if(preprocessor == null){
            preprocessor = new PreprocessorImp();
        }
        return preprocessor;
    }
}
