package com.androidstudy.com.daki;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Base extends Activity{

    // NON: number of notifications
    protected TextView NON;
    protected boolean ok = true;

    protected String sms1;
    protected String sms2;
    protected String exit;

    protected static final String NO_USER = "no_user";
    protected static String user = NO_USER;

    protected static final GestorDDBB db = new GestorDDBB();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_base);
        loadMarc();
        NON = (TextView) this.findViewById(R.id.numberOfNotifications);
        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int notifications = db.getNumberOfNotifications();
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
    public void back(View v){
      //  ok=false;
        finish();
    }

    @Override
    public void onDestroy(){
        if(sms1.equals("d'AKI"))db.desconectar();
        super.onDestroy();
    }

    public void inflateWith(int screen) {
        RelativeLayout box = (RelativeLayout) findViewById(R.id.box);
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(screen, box, true);
    }

}
