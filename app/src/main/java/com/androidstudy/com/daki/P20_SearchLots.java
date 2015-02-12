package com.androidstudy.com.daki;

import android.os.Bundle;

/**
 * Created by Enrique on 09/02/2015.
 */
public class P20_SearchLots extends Base {
    @Override
    protected void onCreate(Bundle b) {
        sms1 = this.user;
        sms2 = "SEARCH NEW LOTS";
        exit = "Back to HOME";
        super.onCreate(b);
        inflateWith(R.layout.inicio);
    }
}
