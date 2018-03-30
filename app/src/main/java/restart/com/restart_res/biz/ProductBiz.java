package restart.com.restart_res.biz;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import restart.com.restart_res.bean.Product;
import restart.com.restart_res.config.Config;
import restart.com.restart_res.net.CommonCallback;

/**
 * Created by Administrator on 2018/3/30.
 */

public class ProductBiz {
    public void listByPage(int currentPage, CommonCallback<List<Product>> commonCallback) {
        OkHttpUtils.post()
                .url(Config.baseUrl + "product_find")
                .addParams("currentPage",currentPage+"")
                .tag(this)
                .build()
                .execute(commonCallback);
    }

    public void onDestory() {
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
