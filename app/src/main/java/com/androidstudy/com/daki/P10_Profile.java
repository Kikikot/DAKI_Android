package com.androidstudy.com.daki;

import android.os.Bundle;

/**
 * Created by Enrique on 09/02/2015.
 */
public class P10_Profile extends Base {
    @Override
    protected void onCreate(Bundle b) {
        sms1 = this.user;
        sms2 = "PROFILE";
        exit = "Back to HOME";
        super.onCreate(b);
        if (this.user == NO_USER) inflateWith(R.layout.l10_perfil_no_user);
        else inflateWith(R.layout.l10_perfil_with_user);
    }
}
