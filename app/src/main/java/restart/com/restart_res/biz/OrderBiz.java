package restart.com.restart_res.biz;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.Map;

import restart.com.restart_res.bean.Order;
import restart.com.restart_res.bean.Product;
import restart.com.restart_res.config.Config;
import restart.com.restart_res.net.CommonCallback;

/**
 * Created by Administrator on 2018/3/29.
 */

public class OrderBiz {
    public void onDestory() {
        OkHttpUtils.getInstance().cancelTag(this);
    }

    public void listByPage(int currentPage, CommonCallback<List<Order>> commonCallback) {
        OkHttpUtils.post()
                .url(Config.baseUrl + "order_find")
                .tag(this)
                .addParams("currentPage",currentPage+"")
                .build()
                .execute(commonCallback);
    }

    public void add(Order order,CommonCallback<String> commonCallback) {
        StringBuilder sb = new StringBuilder();
        Map<Product, Integer> productMap = order.productMap;
        for (Product p :productMap.keySet()) {
            Integer count = productMap.get(p);
            if (count != null && count > 0) {
                sb.append(p.getId() + "_" + productMap.get(p));
                sb.append("|");
            }
        };
        sb.deleteCharAt(sb.length() - 1);


        OkHttpUtils.post()
                .url(Config.baseUrl+"order_add")
                .tag(this)
                .addParams("res_id",order.getRestaurant().getId()+"")
                .addParams("product_str",sb.toString())
                .addParams("count",order.getCount()+"")
                .addParams("price",order.getPrice()+"")
                .build()
                .execute(commonCallback);
    }
}
