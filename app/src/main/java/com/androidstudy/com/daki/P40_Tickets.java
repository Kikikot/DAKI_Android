package com.androidstudy.com.daki;

import android.os.Bundle;

/**
 * Created by Enrique on 09/02/2015.
 */
public class P40_Tickets extends Base {



    @Override
    protected void onCreate(Bundle b) {
        sms1 = this.user;
        sms2 = "MY TICKETS";
        exit = "Back to HOME";
        super.onCreate(b);
        inflateWith(R.layout.l00_home);
    }

    @Override
    public void dialogYESAction() {

    }
}
