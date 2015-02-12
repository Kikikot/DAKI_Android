package com.androidstudy.com.daki;

import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Enrique on 06/02/2015.
 */
public class P99_Notifications extends Base {

    protected TextView numberOfNotifications;

    @Override
    protected void onCreate(Bundle b) {
        this.sms1 = "Notifications";
        this.sms2 = this.user;
        this.exit = "Go back";
        super.onCreate(b);
        TextView temp = (TextView)this.findViewById(R.id.numberOfNotifications);
        temp.setText("2");
    }
}
