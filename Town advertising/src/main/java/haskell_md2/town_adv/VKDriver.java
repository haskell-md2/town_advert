package haskell_md2.town_adv;

import org.apache.http.message.BasicNameValuePair;

public class VKDriver {

    private String _key;

    VKDriver(String key){
        this._key = key;
    }

    void CreatePostInGroup(String msg, String group_id){

        BasicNameValuePair[] params = new BasicNameValuePair[5];
        params[0] = new BasicNameValuePair("owner_id", "-"+group_id);
        params[1] = new BasicNameValuePair("access_token", _key);
        params[2] = new BasicNameValuePair("message", msg);
        params[3] = new BasicNameValuePair("v", "5.131");
        params[4] = new BasicNameValuePair("from_group", "1");

        WebUtils.SendPost("https://api.vk.com/method/wall.post", params);
    }

}
