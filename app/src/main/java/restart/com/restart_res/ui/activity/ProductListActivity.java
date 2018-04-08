package restart.com.restart_res.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import restart.com.restart_res.R;
import restart.com.restart_res.bean.Order;
import restart.com.restart_res.bean.Product;
import restart.com.restart_res.biz.OrderBiz;
import restart.com.restart_res.biz.ProductBiz;
import restart.com.restart_res.net.CommonCallback;
import restart.com.restart_res.ui.adapter.ProductListAdapter;
import restart.com.restart_res.ui.view.refresh.SwipeRefresh;
import restart.com.restart_res.ui.view.refresh.SwipeRefreshLayout;
import restart.com.restart_res.ui.vo.ProductItem;
import restart.com.restart_res.utils.MatchUtil;
import restart.com.restart_res.utils.T;

public class ProductListActivity extends BaseActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTvCount;
    private Button mBtnPay;

    private RecyclerView mRecyclerView;
    private ProductListAdapter mAdapter;
    private List<ProductItem> mDatas = new ArrayList<>();

    private OrderBiz mOrderBiz = new OrderBiz();
    private ProductBiz mProductBiz = new ProductBiz();
    private int mCurrentPage;

    private float mTotalPrice;
    private int mTotalCount;

    private Order mOrder = new Order();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProductBiz.onDestory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        setUpToolbar();
        setTitle("订餐");

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
        mAdapter.setOnProductListener(new ProductListAdapter.OnProductListener() {
            @Override
            public void onProductAdd(ProductItem productItem) {
                mTotalCount++;
                mTvCount.setText("数量:"+mTotalCount);
//                mTotalPrice += productItem.getPrice();
                mTotalPrice = MatchUtil.add(mTotalPrice, productItem.getPrice());
                mBtnPay.setText(mTotalPrice+"元 立即支付");

                mOrder.addProduct(productItem);

            }

            @Override
            public void onProductSub(ProductItem productItem) {
                mTotalCount--;
                mTvCount.setText("数量:"+mTotalCount);
//                mTotalPrice -= productItem.getPrice();
                mTotalPrice = MatchUtil.sub(mTotalPrice, productItem.getPrice());
                if (mTotalCount == 0) {
                    mTotalPrice = 0;
                }

                mBtnPay.setText(mTotalPrice+"元 立即支付");

                mOrder.removeProduct(productItem);

            }
        });
        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTotalCount <= 0) {
                    T.showToast("你还没有选择菜品");
                    return;
                }
                mOrder.setCount(mTotalCount);
                mOrder.setPrice(mTotalPrice);
                mOrder.setRestaurant(mDatas.get(0).getRestaurant());

                startLoadingProgress();

                mOrderBiz.add(mOrder, new CommonCallback<String>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                        if ("用户未登录".equals(e.getMessage())){
                            toLoginActivity();
                        }
                    }

                    @Override
                    public void onSuccess(String response) {
                        stopLoadingProgress();
                        T.showToast("订单支付成功");

                        setResult(RESULT_OK);
                        finish();
                    }
                });
            }
        });

    }

    private void loadMore() {
        startLoadingProgress();
        mProductBiz.listByPage(++mCurrentPage, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                mCurrentPage--;
                Log.e("", "onError: "+e.getMessage(),e );
                mSwipeRefreshLayout.setPullUpRefreshing(false);
                if ("用户未登录".equals(e.getMessage())){
                    toLoginActivity();
                }
            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                mSwipeRefreshLayout.setPullUpRefreshing(false);
                if (response.size() <= 0) {
                    T.showToast("没有了");
                    return;
                }
                T.showToast("又找到"+response.size()+"道菜");
                for (Product product : response) {
                    mDatas.add(new ProductItem(product));
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadDatas() {
        startLoadingProgress();
        mProductBiz.listByPage(0, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                Log.e("", "onError: "+e.getMessage(),e );
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> response) {
                stopLoadingProgress();
                mSwipeRefreshLayout.setRefreshing(false);
                mCurrentPage = 0;
                mDatas.clear();
                for (Product product : response) {
                    mDatas.add(new ProductItem(product));
                }
                mAdapter.notifyDataSetChanged();
                //清空数据
                mTotalPrice = 0;
                mTotalCount = 0;
                mTvCount.setText("数量:"+mTotalCount);
                mBtnPay.setText(mTotalPrice+"元 立即支付");
            }
        });
    }

    private void initView() {
        mSwipeRefreshLayout = findViewById(R.id.id_swiperefresh);
        mTvCount = findViewById(R.id.id_tv_count);
        mBtnPay = findViewById(R.id.id_btn_pay);
        mRecyclerView = findViewById(R.id.id_recyclerview);
        //srl初始化
        mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.GREEN, Color.YELLOW);

        mAdapter = new ProductListAdapter(this, mDatas);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

    }
}
