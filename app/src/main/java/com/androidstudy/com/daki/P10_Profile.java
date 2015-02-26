package com.androidstudy.com.daki;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Enrique on 09/02/2015.
 */
public class P10_Profile extends Base {

    private boolean needChange = false;
    private boolean eraseUserFromDDBB = false;
    private boolean userErased = false;
    private String error = "";
    private static final String path = "p10_perfil/";
    private TextView tSMS1;

    @Override
    protected void onCreate(Bundle b) {
        sms1 = this.user;
        sms2 = "PROFILE";
        exit = "Back to HOME";
        super.onCreate(b);
        tSMS1 = (TextView) this.findViewById(R.id.sms1);
        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        if (this.user.equals(NO_USER)) loadNoUserScreen();
        else loadWithUserScreen();
    }

    private void loadWithUserScreen() {
        if (needChange) {
            RelativeLayout layout = (RelativeLayout)findViewById(R.id.box);
            layout.removeAllViews();
        }
        inflateWith(R.layout.l10_perfil_with_user);
        ((TextView)(findViewById(R.id.tAlias))).setText(this.user);
        ((TextView)(findViewById(R.id.tEmail))).setText(settings.getString(USER_MAIL, ""));
        ((TextView)(findViewById(R.id.tContact))).setText(settings.getString(USER_NAME, "")+" "+settings.getString(USER_SURNAME, ""));
        ((TextView)(findViewById(R.id.tCity))).setText(settings.getString(USER_CITY, ""));
        ((TextView)(findViewById(R.id.tAdress))).setText(settings.getString(USER_ADDRESS, ""));
    }

    private void loadNoUserScreen() {
        needChange=true;
        inflateWith(R.layout.l10_perfil_no_user);
    }

    public void connect(View v){
        initProgres();
        Thread t = new Thread(){
            public void run(){
                connexionRuning();
            }
        };
        t.start();
    }

    private void connexionRuning() {
        TextView tUser = (TextView) this.findViewById(R.id.tUser);
        String user = tUser.getText()+"";

        TextView tPass = (TextView) this.findViewById(R.id.tPass);
        String pass = tPass.getText()+"";

        if (user.equals(NO_USER)){
            showError("Restricted user name. Please, select another.");
        } else if (user.equals("") || pass.equals("")) {
            showError("You need to fill 'User' and 'Pass' fields.");
        } else {
            if (tryConnect(user, pass)){
                progress.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadWithUserScreen();
                    }
                });
            }else{
                showError("User not found. Please, try again");
            }
        }
    }

    private void showError(String s) {
        error = s;
        progress.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast(error);
            }
        });
    }

    private boolean tryConnect(String tryingUser, String pass) {
        ok = false;
        String[] values = {tryingUser, pass};
        String[] parameters = {"user", "pass"};

        phps.DoOnDDBB select = new phps.DoOnDDBB(
                this,
                path + getResources().getString(R.string.php_Login__IN_user_pass__OUT_JSON_id_mail_address_cp_city_name_surname),
                parameters,
                values);
        select.execute();
        try{
            String result = select.get(20, TimeUnit.SECONDS);
            JSONArray answer = new JSONArray(result);
            if (answer.length()==1){
                try {
                    user=tryingUser;
                    sms1 = user;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tSMS1.setText(user);
                        }
                    });
                    JSONObject jRow = answer.getJSONObject(0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(Base.USER_ID, jRow.getString("id"));
                    editor.putString(Base.USER_ALIAS, user);
                    editor.putString(Base.USER_PASS, pass);
                    editor.putString(Base.USER_MAIL, jRow.getString("mail"));
                    editor.putString(Base.USER_ADDRESS, jRow.getString("address"));
                    editor.putString(Base.USER_CP, jRow.getString("cp"));
                    editor.putString(Base.USER_CITY, jRow.getString("city"));
                    editor.putString(Base.USER_NAME, jRow.getString("name"));
                    editor.putString(Base.USER_SURNAME, jRow.getString("surname"));
                    editor.commit();
                    ok = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
        }
        return ok;
    }

    public void contact(View v){
        startActivity(new Intent(P10_Profile.this, P12_Contact.class));
    }

    public void completeData(View v){
        startActivity(new Intent(P10_Profile.this, P11_Register.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (USER_EDITED){
            USER_EDITED = false;
            needChange = true;
            loadWithUserScreen();
            tSMS1.setText(this.user);
        }
    }

    private void eraseUser() {
        String[] values = {this.user, settings.getString(USER_PASS, "")};
        String[] parameters = {"user", "pass"};
        phps.DoOnDDBB select = new phps.DoOnDDBB(
                this,
                path + getResources().getString(R.string.php_eraseUser__IN_user_pass__OUT_STRING_1_OK_0_BAD),
                parameters,
                values);
        select.execute();
        try {
            String result = select.get(10, TimeUnit.SECONDS);
            if (result.equals("0")){
                error = "Something was wrong.";
                userErased = false;
            } else userErased = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            error = "Problems with connexion. Please, try again.";
            userErased = false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            error = "Problems with connexion. Please, try again.";
            userErased = false;
        } catch (TimeoutException e) {
            e.printStackTrace();
            error = "Problems with connexion. Please, try again.";
            userErased = false;
        }
    }

    public void unsubscribe(View v){
        eraseUserFromDDBB = true;
        initRegularDialog("Do you really want to unsubscribe from d'AKI?\n(You will lose all your tickets and participations)");
    }

    public void disconnect(View v){
        eraseUserFromDDBB = false;
        initRegularDialog("Do you really want to disconnect?");
    }

    public void dialogYESAction() {
        error = "";
        initProgres();
        Thread t = new Thread(){
            public void run(){
                if (eraseUserFromDDBB) eraseUser();
                if ((eraseUserFromDDBB && userErased) || (!eraseUserFromDDBB)) clearData();
                progress.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (eraseUserFromDDBB && userErased){
                            toast("User erased without problems.");
                            loadNoUserScreen();
                        } else if (eraseUserFromDDBB && !userErased){
                            toast(error);
                        } else {
                            toast ("User disconnected without problems.");
                            loadNoUserScreen();
                        }
                    }
                });
            }
        };
        t.start();
    }

    private void clearData() {
        this.user = NO_USER;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tSMS1.setText(NO_USER);
            }
        });
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(USER_ALIAS);
        editor.remove(USER_PASS);
        editor.remove(USER_MAIL);
        editor.remove(USER_CITY);
        editor.remove(USER_NAME);
        editor.remove(USER_SURNAME);
        editor.remove(USER_ADDRESS);
        editor.remove(USER_CP);
        editor.commit();
    }
}
