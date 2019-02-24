package pt.eskimi.com.eskimi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naim on 2/24/2019.
 */

public class MainData {

    private String countryName, countryCode, effectiveDate;
    private List<Rate> rateList = new ArrayList<>();

    public MainData(String countryName, String countryCode, String effectiveDate, List<Rate> rateList) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.effectiveDate = effectiveDate;
        this.rateList = rateList;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public List<Rate> getRateList() {
        return rateList;
    }

    public void setRateList(List<Rate> rateList) {
        this.rateList = rateList;
    }
}
