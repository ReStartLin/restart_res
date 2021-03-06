package restart.com.restart_res.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import restart.com.restart_res.R;
import restart.com.restart_res.UserInfoHolder;
import restart.com.restart_res.bean.Order;
import restart.com.restart_res.bean.User;
import restart.com.restart_res.biz.OrderBiz;
import restart.com.restart_res.net.CommonCallback;
import restart.com.restart_res.ui.adapter.OrderAdapter;
import restart.com.restart_res.ui.view.CircleTransform;
import restart.com.restart_res.ui.view.refresh.SwipeRefresh;
import restart.com.restart_res.ui.view.refresh.SwipeRefreshLayout;
import restart.com.restart_res.utils.T;

public class OrderActivity extends BaseActivity {
    private Button mBtnOrder;
    private TextView mTvUsername;
    private RecyclerView mRecyclerView;
    private OrderAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mIvIcon;

    private List<Order> mDatas = new ArrayList<>();
    private OrderBiz mOrderBiz = new OrderBiz();

    private int mCurrentPage = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrderBiz.onDestory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initView();
        initEvent();
        loadDatas();
    }

    private void initEvent() {
        //下拉
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();
            }
        });
        //上拉
        mSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();
            }
        });

        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, ProductListActivity.class);
                startActivityForResult(intent,1001);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            loadDatas();
        }
    }

    private void loadMore() {
        startLoadingProgress();
        mOrderBiz.listByPage(++mCurrentPage, new CommonCallback<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mCurrentPage--;
                mSwipeRefreshLayout.setPullUpRefreshing(false);
            }

            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();
                if (response.size() == 0) {
                    T.showToast("没有更多订单了");
                    mSwipeRefreshLayout.setPullUpRefreshing(false);
                    return;
                }
                T.showToast("订单加载成功");
                mDatas.addAll(response);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setPullUpRefreshing(false);
            }
        });

    }

    private void loadDatas() {
        startLoadingProgress();
        mOrderBiz.listByPage(0, new CommonCallback<List<Order>>() {
            @Override
            public void onError(Exception e) {
                Log.e("", "onError: "+e.getMessage(), e);
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);

                if ("用户未登录".equals(e.getMessage())){
                    toLoginActivity();
                }
            }

            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();
                T.showToast("订单更新成功");
                mCurrentPage = 0;
                mDatas.clear();
                mDatas.addAll(response);
                mAdapter.notifyDataSetChanged();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
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
        mAdapter = new OrderAdapter(mDatas, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        Picasso.with(this).load(R.drawable.icon)
                .placeholder(R.drawable.pictures_no)
                .transform(new CircleTransform())
                .into(mIvIcon);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            } catch (Exception e) {
                //ignore
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
