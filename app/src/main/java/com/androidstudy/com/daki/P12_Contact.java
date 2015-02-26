package com.androidstudy.com.daki;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import phps.DoOnDDBB;

/**
 * Created by Enrique on 17/02/2015.
 */
public class P12_Contact extends Base {

    private String error;
    private EditText message;
    private TextView count;
    private static final int top = 500;

    private static final String path = "p12_contact/";

    @Override
    protected void onCreate(Bundle b) {
        sms1 = this.user;
        sms2 = "CONTACT";
        exit = "Back to PROFILE";
        super.onCreate(b);
        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        inflateWith(R.layout.l12_contact);
        message = (EditText)findViewById(R.id.tMessage);
        count = (TextView)findViewById(R.id.i);
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = message.getText().toString();
                int length = text.length();
                if (length>top){
                    text = text.substring(0, top);
                    message.setText(text);
                }
                count.setText(text.length()+" / "+top);
            }
        });
    }

    @Override
    public void dialogYESAction() {

    }

    public void sendMessage(View v){
        initProgres();
        Thread t = new Thread(){
            public void run(){
                if (allParamsOK()){
                    if (sendMessagePHP()){
                        progress.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                message.setText("");
                                count.setText("0 / "+top);
                                ((EditText)findViewById(R.id.tMail)).setText("");
                                ((EditText)findViewById(R.id.tSubject)).setText("");
                                toast("Message dispatched without problems.");
                            }
                        });
                    }else showErrors();
                } else showErrors();
            }
        };
        t.start();
    }

    private boolean sendMessagePHP() {
        String subject = ((EditText)findViewById(R.id.tSubject)).getText().toString();
        String email = ((EditText)findViewById(R.id.tMail)).getText().toString();
        String text = message.getText().toString();
        String senderUser = user;
        if (user.equals(NO_USER)) senderUser = "null";
        String[] values = {senderUser, subject, email, text};
        String[] params = {"user", "subject", "mail", "message"};
        phps.DoOnDDBB toDo = new phps.DoOnDDBB(
                this,
                path + getResources().getString(R.string.php_SendMessage__IN_user_subject_mail_message__OUT_STRING_1_OK_0_BAD),
                params,
                values
        );
        toDo.execute();
        try {
            String answer = toDo.get(20, TimeUnit.SECONDS);
            if (answer.equals("1")) return true;
            error = "Something was wrong... Please, try again.";
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

    private boolean allParamsOK() {
        error = "";
        int subject = ((EditText)findViewById(R.id.tSubject)).getText().toString().length();
        int email = ((EditText)findViewById(R.id.tMail)).getText().toString().length();
        int text = message.getText().toString().length();
        if (email!=0 && subject!=0 && text!=0) return true;
        error = "Please, fill all params.";
        return false;
    }

    private void showErrors() {
        progress.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toast(error);
            }
        });
    }
}
