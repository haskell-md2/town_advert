package haskell_md2.town_adv;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class WebUtils {

    static void SendPost(String url, BasicNameValuePair[] arg_params){
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);

// Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);

        for(BasicNameValuePair par : arg_params){
            params.add(par);
        }
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//Execute and get the response.
        HttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            try (InputStream instream = entity.getContent()) {
                // do something useful
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
