package restart.com.restart_res.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import restart.com.restart_res.R;
import restart.com.restart_res.UserInfoHolder;
import restart.com.restart_res.bean.User;
import restart.com.restart_res.ui.adapter.OrderAdapter;
import restart.com.restart_res.ui.view.CircleTransform;
import restart.com.restart_res.ui.view.refresh.SwipeRefresh;
import restart.com.restart_res.ui.view.refresh.SwipeRefreshLayout;

public class OrderActivity extends BaseActivity {
    private Button mBtnOrder;
    private TextView mTvUsername;
    private RecyclerView mRecyclerView;
    private OrderAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mIvIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initView();
    }

    private void initView() {
        mBtnOrder = findViewById(R.id.id_btn_order);
        mTvUsername = findViewById(R.id.id_tv_username);
        mRecyclerView = findViewById(R.id.id_recyclerview);
        mSwipeRefreshLayout = findViewById(R.id.id_swiperefresh);
        mIvIcon = findViewById(R.id.id_iv_icon);

        User user = UserInfoHolder.getInstance().getUser();
        if (user != null) {
            mTvUsername.setText(user.getUsername());

        } else {
            toLoginActivity();
            return;
        }
        mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.GREEN, Color.YELLOW);

        //recyclerview
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        Picasso.with(this).load(R.drawable.icon)
                .placeholder(R.drawable.pictures_no)
                .transform(new CircleTransform())
                .into(mIvIcon);

    }
}
