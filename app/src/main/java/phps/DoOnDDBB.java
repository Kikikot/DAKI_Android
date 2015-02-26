package phps;

import android.os.AsyncTask;
import android.os.Looper;

import com.androidstudy.com.daki.Base;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Enrique on 15/02/2015.
 */
public class DoOnDDBB extends AsyncTask<String,String,String> {

    private Base base;
    private String file;
    private String[] parameters;
    private String[] values;

    public DoOnDDBB(Base base, String file, String[] parameters, String[] values) {
        this.base = base;
        this.file = file;
        this.parameters = parameters;
        if (values==null) this.values = new String[0];
        else this.values = values;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient client =new DefaultHttpClient();
        ArrayList<NameValuePair> nameValuePairs;
        HttpPost httppost = new HttpPost(FONTS.HOST+file);
        String result;

        try {
            if (values.length>0) {
                nameValuePairs = new ArrayList<NameValuePair>(values.length);
                for (int i=0; i<values.length; i++){
                    nameValuePairs.add(new BasicNameValuePair(parameters[i], values[i].trim()));
                }
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }
            HttpResponse response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }

}
