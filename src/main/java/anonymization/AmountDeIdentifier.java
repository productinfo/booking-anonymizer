package anonymization;

public class AmountDeIdentifier implements DeIdentifier{

    private static final int MAXIMUM_BIN_COUNT = 20;

    @Override
    public String getDeIdentifiedText(final String text) {
        if(text == null){
            return "NaN";
        }
        double amount = getAmountFromText(text);
        if(Double.isNaN(amount)){
            return "NaN";
        }
        return getDeIdentifiedAmount(amount);
    }

    private String getDeIdentifiedAmount(final double amount){
        if(amount == 0){
            return "0";
        }

        String amountSign = "";
        double unsignedAmount = amount;

        if(amount < 0){
            amountSign = "-";
            unsignedAmount *= -1;
        }
        int binCounter = 1;
        while(Math.pow(2, binCounter) < unsignedAmount && binCounter <= MAXIMUM_BIN_COUNT){
            binCounter++;
        }
        return amountSign + binCounter;
    }

    private double getAmountFromText(String text){
        try{
            return Double.parseDouble(text.trim());
        }catch (NumberFormatException e){
            return Double.NaN;
        }
    }
}
