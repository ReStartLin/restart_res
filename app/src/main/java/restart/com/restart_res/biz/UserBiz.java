package restart.com.restart_res.biz;

import com.zhy.http.okhttp.OkHttpUtils;

import restart.com.restart_res.bean.User;
import restart.com.restart_res.config.Config;
import restart.com.restart_res.net.CommonCallback;

/**
 * Created by zhy on 16/10/23.
 */
public class UserBiz {

    public void onDestory() {
        OkHttpUtils.getInstance().cancelTag(this);
    }

    public void register(String username, String password, CommonCallback<User> callBack) {
        OkHttpUtils
                .post()
                .url(Config.baseUrl + "user_register")
                .tag(this)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(callBack);
    }


    public void login(String username, String password, CommonCallback<User> callBack) {

        OkHttpUtils
                .post()
                .url(Config.baseUrl + "user_login")
                .tag(this)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(callBack);
    }

}
