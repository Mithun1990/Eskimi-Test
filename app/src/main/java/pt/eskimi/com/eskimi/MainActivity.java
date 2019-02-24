package pt.eskimi.com.eskimi;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.eskimi.com.eskimi.interfaceIml.GetDataImpl;
import pt.eskimi.com.eskimi.interfacepackage.DataView;
import pt.eskimi.com.eskimi.model.MainData;
import pt.eskimi.com.eskimi.model.Rate;
import pt.eskimi.com.eskimi.presenter.GetDataPresenter;
import pt.eskimi.com.eskimi.utils.JsonParse;

public class MainActivity extends AppCompatActivity implements DataView {

    private GetDataPresenter getDataPresenter;
    List<MainData> mainDataList = new ArrayList<>();
    List<String> countryList = new ArrayList<>();
    private Spinner spinnerCountry;
    private int spinnerPosition = -1;
    private LinearLayout layoutRadioButton;
    private RadioGroup layoutRadioGroup;
    private TextView textViewTotalAmount;
    private EditText etAmount;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        getDataFromServer();

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int radioButtonPosition = layoutRadioGroup.getCheckedRadioButtonId();

                try {
                    String str = charSequence.toString();
                    double mainBalance = 0;
                    if (str.length() == 0) {
                        mainBalance = 0;
                    } else {
                        double extValue = Double.parseDouble(str);

                        if (extValue > 0) {
                            List<Rate> rates = mainDataList.get(spinnerPosition).getRateList();
                            mainBalance = extValue + Double.parseDouble(rates.get(radioButtonPosition).getRateValue());
                        } else {
                            mainBalance = 0;
                        }

                    }


                    textViewTotalAmount.setText("Total Amount is " + mainBalance);

                } catch (NumberFormatException e) {

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void initComponents() {
        getDataPresenter = new GetDataImpl(this, this);
        spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        layoutRadioButton = (LinearLayout) findViewById(R.id.layoutRadioButton);
        textViewTotalAmount = (TextView) findViewById(R.id.totalAmount);
        etAmount = (EditText) findViewById(R.id.etAmount);
        layoutRadioGroup = new RadioGroup(this);
        layoutRadioGroup.setOrientation(LinearLayout.VERTICAL);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Syncing with server...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    /**
     * get data from remote server
     **/
    private void getDataFromServer() {
        progressDialog.show();
        getDataPresenter.getDataFromSever();
    }

    private void initCountrySpinner() {


        try {
            ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_item, countryList);
            spinnerCountry.setAdapter(adapter);


        } catch (Exception e) {
            e.printStackTrace();
        }


        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //     System.out.println("Pos " + position);

                try {

                    spinnerPosition = position;
                    initRadioButton();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void initRadioButton() {

        if (spinnerPosition == -1)
            return;


        int radioButtonListSize = mainDataList.get(spinnerPosition).getRateList().size();
        List<Rate> rates = mainDataList.get(spinnerPosition).getRateList();
        layoutRadioGroup.removeAllViews();

        for (int i = 0; i < radioButtonListSize; i++) {

            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setId(i);
            rdbtn.setText(rates.get(i).getRateTitle());
            if (i == 0)
                rdbtn.setChecked(true);
            layoutRadioGroup.addView(rdbtn);


        }

        layoutRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (etAmount.getText().toString().isEmpty())
                    return;

                String str = etAmount.getText().toString();
                double extValue = Double.parseDouble(str);
                double mainBalance = 0;
                if (extValue > 0) {
                    List<Rate> rates = mainDataList.get(spinnerPosition).getRateList();
                    mainBalance = extValue + Double.parseDouble(rates.get(i).getRateValue());
                } else {
                    mainBalance = 0;
                }

                textViewTotalAmount.setText("Total Amount is " + mainBalance);

            }
        });


        ((ViewGroup) findViewById(R.id.radioGroup)).addView(layoutRadioGroup);
    }

    @Override
    public void onSuccessGetData(Context context, JSONObject jsonObject) {

        if (progressDialog.isShowing())
            progressDialog.dismiss();

        try {
            if (JsonParse.checkJson("rates", jsonObject)) {
                JSONArray jsonArray = jsonObject.getJSONArray("rates");
                mainDataList.clear();
                countryList.clear();

                for (int i = 0; i < jsonArray.length(); i++) {

                    String countryName = "", countryCode = "", effectiveDate = "";

                    JSONObject json = jsonArray.getJSONObject(i);

                    if (JsonParse.checkJson("name", json)) {
                        countryName = json.getString("name");
                    }

                    if (JsonParse.checkJson("code", json)) {
                        countryCode = json.getString("code");
                    }


                    if (JsonParse.checkJson("periods", json)) {

                        JSONArray jsonPeriods = json.getJSONArray("periods");
                        List<Rate> rates = new ArrayList<>();
                        for (int j = 0; j < 1; j++) {
                            JSONObject jsPrd = jsonPeriods.getJSONObject(j);

                            if (JsonParse.checkJson("effective_from", jsPrd)) {
                                effectiveDate = jsPrd.getString("effective_from");
                            }

                            if (JsonParse.checkJson("rates", jsPrd)) {
                                JSONObject jsonRates = jsPrd.getJSONObject("rates");
                                Iterator jsonKeys = jsonRates.keys();
                                while (jsonKeys.hasNext()) {
                                    String key = (String) jsonKeys.next();
                                    String value = jsonRates.getString(key);
                                    rates.add(new Rate(key, value));
                                }

                            }


                        }
                        MainData mainData = new MainData(countryName, countryCode, effectiveDate, rates);
                        mainDataList.add(mainData);
                        countryList.add(countryName);
                    }


                }

                if (mainDataList.size() > 0) {
                    initCountrySpinner();
                }
                System.out.println("Main Data Size " + mainDataList.size());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailureGetData(Context context, int errorCode, String message, JSONObject jsonObject) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
