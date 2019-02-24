package pt.eskimi.com.eskimi.model;

/**
 * Created by Naim on 2/24/2019.
 */

public class Rate {
    private String rateTitle;
    private String rateValue;

    public Rate(String rateTitle, String rateValue) {
        this.rateTitle = rateTitle;
        this.rateValue = rateValue;
    }

    public String getRateTitle() {
        return rateTitle;
    }

    public void setRateTitle(String rateTitle) {
        this.rateTitle = rateTitle;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }
}
