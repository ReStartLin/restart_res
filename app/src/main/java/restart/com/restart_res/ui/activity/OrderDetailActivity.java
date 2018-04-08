package restart.com.restart_res.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import restart.com.restart_res.R;
import restart.com.restart_res.bean.Order;
import restart.com.restart_res.config.Config;
import restart.com.restart_res.utils.T;

public class OrderDetailActivity extends BaseActivity {
    private Order order;

    private ImageView mIvImage;
    private TextView mTvTitle;
    private TextView mTvDesc;
    private TextView mTvPrice;
    public static final String KEY_ORDER = "key_order";
    public static void launch(Context context, Order order) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(KEY_ORDER, order);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        setUpToolbar();
        setTitle("订单详情");
        Intent intent = getIntent();
        if (intent != null) {
            order = (Order) intent.getSerializableExtra(KEY_ORDER);
        }
        if (order == null) {
            T.showToast("参数传递错误");
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        mIvImage = findViewById(R.id.id_iv_image);
        mTvTitle = findViewById(R.id.id_tv_title);
        mTvDesc = findViewById(R.id.id_tv_desc);
        mTvPrice = findViewById(R.id.id_tv_price);

        Picasso.with(this)
                .load(Config.baseUrl+order.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(mIvImage);
        mTvTitle.setText(order.getRestaurant().getName());

        List<Order.ProductVo> ps = order.getPs();
        StringBuilder sb = new StringBuilder();
        for (Order.ProductVo p : ps) {
            sb.append(p.product.getName())
                    .append("*")
                    .append(p.count)
                    .append("\n");
        }
        mTvDesc.setText(sb.toString());
        mTvPrice.setText("共消费"+order.getPrice()+"元");
    }
}
