package com.androidstudy.com.daki;

import android.os.Bundle;

/**
 * Created by Enrique on 09/02/2015.
 */
public class P30_MyLots extends Base {
    @Override
    protected void onCreate(Bundle b) {
        sms1 = this.user;
        sms2 = "MY LOTS";
        exit = "Back to HOME";
        super.onCreate(b);
        inflateWith(R.layout.inicio);
    }
}
