package com.androidstudy.com.daki;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Enrique on 06/02/2015.
 */
public class P00_Home extends Base{
    @Override
    protected void onCreate(Bundle b) {
        sms1 = "d'AKI";
        sms2 = "Regalos por la cara";
        exit = "Finish";
        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        user=settings.getString(USER_ALIAS, NO_USER);
        super.onCreate(b);
        inflateWith(R.layout.l00_home);
    }

    @Override
    public void dialogYESAction() {

    }

    public void menuOptionPressed(View v){
        TextView t = (TextView) v;
        String option = t.getText()+"";
        if (option.equalsIgnoreCase(getResources().getString(R.string.p00_user))){
            startActivity(new Intent(this, P10_Profile.class));
        } else if (this.user.equals(NO_USER)){
            toast("Please, log first.");
        } else {
            if (option.equalsIgnoreCase(getResources().getString(R.string.p00_new_lots)))
                startActivity(new Intent(this, P20_SearchLots.class));
            else if (option.equalsIgnoreCase(getResources().getString(R.string.p00_my_lots)))
                startActivity(new Intent(this, P30_MyLots.class));
            else startActivity(new Intent(this, P40_Tickets.class));
        }
    }
}
