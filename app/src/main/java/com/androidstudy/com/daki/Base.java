package com.androidstudy.com.daki;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class Base extends Activity{

    protected TextView NON;
    protected boolean ok = true;

    protected String sms1;
    protected String sms2;
    protected String exit;

    public SharedPreferences settings;
    public ProgressDialog progress;

    protected static final String NO_USER = "no_user";
    protected static String user = NO_USER;
    protected static boolean USER_EDITED = false;

    public static final String SETTINGS = "Settings";

    public static final String USER_ID = "id";
    public static final String USER_ALIAS = "alias";
    public static final String USER_PASS = "pass";
    public static final String USER_MAIL = "mail";
    public static final String USER_ADDRESS = "address";
    public static final String USER_CP = "cp";
    public static final String USER_CITY = "city";
    public static final String USER_NAME = "name";
    public static final String USER_SURNAME = "surname";

    private static final int YES_NO_DIALOG = 0;
    private static String textDialog;

    @Override
    protected void onCreate(Bundle b) {
        try {Class.forName("android.os.AsyncTask");} catch(Throwable ignore) {}
        super.onCreate(b);
        setContentView(R.layout.activity_base);
        loadMarc();
        NON = (TextView) this.findViewById(R.id.numberOfNotifications);
      /*  Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int notifications = getNumberOfNotifications();
                                String text = notifications+"";
                                if (notifications==0) text="";
                                if (!((String)NON.getText()).equals(text))NON.setText(text);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
        */
    }

    public void initProgres() {
        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    private int getNumberOfNotifications() {
        return 0;
    }

    private void loadMarc() {
        TextView sms = (TextView) this.findViewById(R.id.sms1);
        sms.setText(sms1);
        sms = (TextView) this.findViewById(R.id.sms2);
        sms.setText(sms2);
        Button btn = (Button) this.findViewById(R.id.btnSalir);
        btn.setText(exit);
    }

    public void notifications(View v){
        if (sms1.equals("Notifications"))toast("You are in Notifications");
        else startActivity(new Intent(Base.this, P99_Notifications.class));
    }

    public void toast(String text){Toast.makeText(this, text, Toast.LENGTH_LONG).show();}
    public void log(String text){Log.i("- "+this.getClass().getName()+":", "\n"+text);}
    public void back(View v){
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public void inflateWith(int screen) {
        RelativeLayout box = (RelativeLayout) findViewById(R.id.box);
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(screen, box, true);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case YES_NO_DIALOG:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(textDialog);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialogYESAction();
                        Base.this.removeDialog(YES_NO_DIALOG);
                    }
                });
                builder.setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Base.this.removeDialog(YES_NO_DIALOG);
                            }
                        });
                AlertDialog resetDialog = builder.create();
                return resetDialog;
        }
        return null;
    }

    public abstract void dialogYESAction();

    protected void initRegularDialog(String text) {
        textDialog = text;
        showDialog(YES_NO_DIALOG);
    }
}
