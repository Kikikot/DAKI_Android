package com.androidstudy.com.daki;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Enrique on 17/02/2015.
 */
public class P11_Register extends Base {

    private Spinner spinnerProvinces;
    private Spinner spinnerCities;

    private ArrayAdapter<String> adapter;
    private int pos;

    private String error;

    private static final String path = "p11_registrar/";

    @Override
    protected void onCreate(Bundle b) {
        sms1 = user;
        sms2 = "EDIT PROFILE";
        String textButton = "Update Information";
        if (user.equals(NO_USER)){
            sms2 = "SIGN IN";
            textButton = "Sign in";
        }
        exit = "Back to PROFILE";
        super.onCreate(b);
        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        inflateWith(R.layout.l11_register);
        ((Button)findViewById(R.id.sendData)).setText(textButton);
        if (!user.equals(NO_USER))
            (findViewById(R.id.tUser)).setEnabled(false);
        spinnerProvinces = (Spinner) this.findViewById(R.id.tProvince);
        spinnerCities = (Spinner) this.findViewById(R.id.tCity);
        init();
    }

    private void init() {
        initProgres();
        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    String provinces = getProvinces();
                    showProvinces(provinces);
                    pos = 0;
                    String userProvince = adapter.getItem(0);
                    if (!user.equals(NO_USER)){
                        userProvince = getUserProvince();
                        pos = adapter.getPosition(userProvince);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinnerProvinces.setSelection(pos);
                        }
                    });
                    String cities = getCities(userProvince);
                    showCities(cities);
                    if (!user.equals(NO_USER)){
                        pos = adapter.getPosition(settings.getString(USER_CITY,""));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                spinnerCities.setSelection(pos);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                spinnerProvinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        initProgres();
                        Thread t = new Thread(){
                            @Override
                            public void run() {
                                try {
                                    String cities = getCities((String)spinnerProvinces.getSelectedItem());
                                    showCities(cities);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                progress.dismiss();
                            }
                        };
                        t.start();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                progress.dismiss();
            }
        };
        t.start();
        ((EditText)findViewById(R.id.tUser)).setText(settings.getString(USER_ALIAS, NO_USER));
        ((EditText)findViewById(R.id.tPass)).setText(settings.getString(USER_PASS, ""));
        ((EditText)findViewById(R.id.tConfirmPass)).setText(settings.getString(USER_PASS, ""));
        ((EditText)findViewById(R.id.tMail)).setText(settings.getString(USER_MAIL, ""));
        ((EditText)findViewById(R.id.tName)).setText(settings.getString(USER_NAME, ""));
        ((EditText)findViewById(R.id.tSurnames)).setText(settings.getString(USER_SURNAME, ""));
        ((EditText)findViewById(R.id.tAddress)).setText(settings.getString(USER_ADDRESS, ""));
        ((EditText)findViewById(R.id.tCP)).setText(settings.getString(USER_CP, ""));
        ((CheckBox)findViewById(R.id.agree)).setChecked(!user.equals(NO_USER));
    }

    public String getUserProvince() throws InterruptedException, ExecutionException, TimeoutException {
        String[] parameters = {"city"};
        String[] values = {settings.getString(USER_CITY, "")};
        phps.DoOnDDBB select = new phps.DoOnDDBB(
                this,
                path + getResources().getString(R.string.php_GetProvinceOfCity__IN_city__OUT_STRING_province),
                parameters,
                values);
        select.execute();
        return select.get(20, TimeUnit.SECONDS);
    }

    public void showCities(String cities) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinnerCities.removeAllViewsInLayout();
            }
        });
        JSONArray c = new JSONArray(cities);
        String[] items = new String[c.length()];
        for (int i=0; i<c.length(); i++){
            items[i] = c.getJSONObject(i).getString("city");
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,items);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinnerCities.setAdapter(adapter);
            }
        });

    }

    public String getCities(String userProvince) throws InterruptedException, ExecutionException, TimeoutException {
        String[] parameters = {"province"};
        String[] values = {userProvince};
        phps.DoOnDDBB select = new phps.DoOnDDBB(
                this,
                path + getResources().getString(R.string.php_GetCities__IN_province__OUT_JSONARRAY_cities_AS_city),
                parameters,
                values);
        select.execute();
        return select.get(20, TimeUnit.SECONDS);
    }

    public void showProvinces(String provinces) throws JSONException {
        JSONArray p = new JSONArray(provinces);
        String[] items = new String[p.length()];
        for (int i=0; i<p.length(); i++){
            items[i] = p.getJSONObject(i).getString("province");
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,items);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinnerProvinces.setAdapter(adapter);
            }
        });
    }

    public String getProvinces() {
        String[] values = {};
        String[] params = {};
        phps.DoOnDDBB select = new phps.DoOnDDBB(
                this,
                path + getResources().getString(R.string.php_GetProvinces__IN_null__OUT_JSONARRAY_provinces_AS_province),
                values,
                params);
        select.execute();
        String answer = "";
        try {
            answer = select.get(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return answer;
    }

    public void sendData(View v){
        if (user.equals(NO_USER)) signIn();
        else updateData();
    }

    private void updateData() {
        initProgres();
        Thread t = new Thread(){
            public void run(){
                error = "";
                if (dataOK()) {
                    if (sendEditedData()){
                        progress.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                USER_EDITED = true;
                                updateSharedPreferences();
                                toast("User edited without problems.");
                            }
                        });
                    } else showError();
                } else showError();
            }
        };
        t.start();
    }

    private boolean sendEditedData() {
        String tUser = ((EditText)findViewById(R.id.tUser)).getText().toString();
        String tPass = ((EditText)findViewById(R.id.tPass)).getText().toString();
        String tMail = ((EditText)findViewById(R.id.tMail)).getText().toString();
        String tCity = (String)spinnerCities.getSelectedItem();
        String tName = ((EditText)findViewById(R.id.tName)).getText().toString();
        String tSurnames = ((EditText)findViewById(R.id.tSurnames)).getText().toString();
        String tAddress = ((EditText)findViewById(R.id.tAddress)).getText().toString();
        String tCP = ((EditText)findViewById(R.id.tCP)).getText().toString();
        try {
            String[] parameters = {"user", "pass", "mail", "city", "name", "surname", "address", "cp"};
            String[] values = {tUser, tPass, tMail, tCity, tName, tSurnames, tAddress, tCP};
            phps.DoOnDDBB select = new phps.DoOnDDBB(
                    this,
                    path + getResources().getString(R.string.php_editUser__IN_user_pass_mail_city_name_surname_address_cp__OUT_STRING_1_OK_0_BAD),
                    parameters,
                    values);
            select.execute();
            String answer = select.get(20, TimeUnit.SECONDS);
            if (answer.equals("1")) return true;
            else error = "Something has gone wrong. Please, try again.";
        } catch (InterruptedException e) {
            e.printStackTrace();
            error = "Problems with connexion. Please, try again.";
        } catch (ExecutionException e) {
            e.printStackTrace();
            error = "Problems with connexion. Please, try again.";
        } catch (TimeoutException e) {
            e.printStackTrace();
            error = "Problems with connexion. Please, try again.";
        }
        return false;
    }

    private boolean dataOK() {
        String tPass = ((EditText)findViewById(R.id.tPass)).getText().toString();
        String tConfirmPass = ((EditText)findViewById(R.id.tConfirmPass)).getText().toString();
        String tMail = ((EditText)findViewById(R.id.tMail)).getText().toString();
        String tName = ((EditText)findViewById(R.id.tName)).getText().toString();
        String tSurnames = ((EditText)findViewById(R.id.tSurnames)).getText().toString();
        String tAddress = ((EditText)findViewById(R.id.tAddress)).getText().toString();
        String tCP = ((EditText)findViewById(R.id.tCP)).getText().toString();
        boolean check = ((CheckBox)findViewById(R.id.agree)).isChecked();

        if (tPass.equals(settings.getString(USER_PASS, "")) && tMail.equals(settings.getString(USER_MAIL, ""))
                && tName.equals(settings.getString(USER_NAME, "")) && tSurnames.equals(settings.getString(USER_SURNAME, ""))
                && tAddress.equals(settings.getString(USER_ADDRESS, "")) && tCP.equals(settings.getString(USER_CP, ""))
                && !check){
            error = "Please, modify some parameter.";
        }else{
            if (tPass.equals(tConfirmPass)) return true;
            else error = "Pass and Confirmation are different. Please, check it.";
        }
        return false;
    }

    private void signIn() {
        initProgres();
        Thread t = new Thread() {
            public void run(){
                error="";
                if(newDataOK()){
                    if (isNotRegisteredUser()) {
                        if (sendNewUser()) {
                            progress.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    USER_EDITED = true;
                                    updateSharedPreferences();
                                    ((TextView)findViewById(R.id.sms1)).setText(((EditText) findViewById(R.id.tUser)).getText().toString());
                                    ((EditText)findViewById(R.id.tUser)).setEnabled(false);
                                    toast("User registered without problems.");
                                }
                            });
                        } else showError();
                    } else showError();
                } else showError();
            }
        };
        t.start();
    }

    private void updateSharedPreferences() {
        SharedPreferences.Editor editor = settings.edit();
        Base.user = ((EditText)findViewById(R.id.tUser)).getText().toString();
        editor.putString(USER_ALIAS, Base.user);
        editor.putString(USER_PASS, ((EditText)findViewById(R.id.tPass)).getText().toString());
        editor.putString(USER_MAIL, ((EditText)findViewById(R.id.tMail)).getText().toString());
        editor.putString(USER_CITY, (String)spinnerCities.getSelectedItem());
        editor.putString(USER_NAME, ((EditText)findViewById(R.id.tName)).getText().toString());
        editor.putString(USER_SURNAME, ((EditText)findViewById(R.id.tSurnames)).getText().toString());
        editor.putString(USER_ADDRESS, ((EditText)findViewById(R.id.tAddress)).getText().toString());
        editor.putString(USER_CP, ((EditText)findViewById(R.id.tCP)).getText().toString());
        editor.commit();
    }

    private boolean newDataOK() {
        String tUser = ((EditText)findViewById(R.id.tUser)).getText().toString();
        if (!tUser.equals(NO_USER)){
            String tPass = ((EditText)findViewById(R.id.tPass)).getText().toString();
            String tConfirmPass = ((EditText)findViewById(R.id.tConfirmPass)).getText().toString();
            String tMail = ((EditText)findViewById(R.id.tMail)).getText().toString();
            String tName = ((EditText)findViewById(R.id.tName)).getText().toString();
            String tSurnames = ((EditText)findViewById(R.id.tSurnames)).getText().toString();
            String tAddress = ((EditText)findViewById(R.id.tAddress)).getText().toString();
            String tCP = ((EditText)findViewById(R.id.tCP)).getText().toString();
            boolean check = ((CheckBox)findViewById(R.id.agree)).isChecked();

            if (tUser.equals("") || tPass.equals("") || tMail.equals("") || tName.equals("")
                    || tSurnames.equals("") || tAddress.equals("") || tCP.equals("") || !check){
                error = "Please, fill all the parameters.";
            }else{
                if (tPass.equals(tConfirmPass)) return true;
                else error = "Pass and Confirmation are different. Please, check it.";
            }
        } else error = "'"+NO_USER+"' is not a User name. Please, take another.";
        return false;
    }

    private boolean sendNewUser() {
        String tUser = ((EditText)findViewById(R.id.tUser)).getText().toString();
        String tPass = ((EditText)findViewById(R.id.tPass)).getText().toString();
        String tMail = ((EditText)findViewById(R.id.tMail)).getText().toString();
        String tCity = (String)spinnerCities.getSelectedItem();
        String tName = ((EditText)findViewById(R.id.tName)).getText().toString();
        String tSurnames = ((EditText)findViewById(R.id.tSurnames)).getText().toString();
        String tAddress = ((EditText)findViewById(R.id.tAddress)).getText().toString();
        String tCP = ((EditText)findViewById(R.id.tCP)).getText().toString();
        try {
            String[] parameters = {"user", "pass", "mail", "city", "name", "surname", "address", "cp"};
            String[] values = {tUser, tPass, tMail, tCity, tName, tSurnames, tAddress, tCP};
            phps.DoOnDDBB select = new phps.DoOnDDBB(
                    this,
                    path + getResources().getString(R.string.php_registerUser__IN_user_pass_mail_city_name_surname_address_cp__OUT_STRING_1_OK_0_BAD),
                    parameters,
                    values);
            select.execute();
            String answer = select.get(20, TimeUnit.SECONDS);
            if (answer.equals("1")) return true;
            else error = "Something has gone wrong. Please, try again.";
        } catch (InterruptedException e) {
            e.printStackTrace();
            error = "Problems with connexion. Please, try again.";
        } catch (ExecutionException e) {
            e.printStackTrace();
            error = "Problems with connexion. Please, try again.";
        } catch (TimeoutException e) {
            e.printStackTrace();
            error = "Problems with connexion. Please, try again.";
        }
        return false;
    }

    private boolean isNotRegisteredUser() {
        String tUser = ((EditText)findViewById(R.id.tUser)).getText().toString();
        try {
            String[] parameters = {"user"};
            String[] values = {tUser};
            phps.DoOnDDBB select = new phps.DoOnDDBB(
                    this,
                    path + getResources().getString(R.string.php_checkUserExists__IN_user__OUT_STRING_0_OK_1_BAD),
                    parameters,
                    values);
            select.execute();
            String answer = select.get(20, TimeUnit.SECONDS);
            if (answer.equals("0")) return true;
            else error = "This user already exists. Please, try another.";
        } catch (InterruptedException e) {
            e.printStackTrace();
            error = "Problems with connexion. Please, try again.";
        } catch (ExecutionException e) {
            e.printStackTrace();
            error = "Problems with connexion. Please, try again.";
        } catch (TimeoutException e) {
            e.printStackTrace();
            error = "Problems with connexion. Please, try again.";
        }
        return false;
    }

    private void showError() {
        progress.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast(error);
            }
        });
    }

    public void showConditions(View v){
        initRegularDialog("Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now..." +
                " Probably it will be a lot of conditions, but not now...");
    }

    @Override
    public void dialogYESAction() {
        ((CheckBox)findViewById(R.id.agree)).setChecked(true);
    }
}