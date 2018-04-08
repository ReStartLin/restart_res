package restart.com.restart_res.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import restart.com.restart_res.R;
import restart.com.restart_res.bean.Order;
import restart.com.restart_res.config.Config;
import restart.com.restart_res.ui.activity.OrderDetailActivity;

/**
 * Created by Administrator on 2018/3/29.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderItemViewHolder> {
    private List<Order> mDatas;
    private Context mContext;
    private LayoutInflater inflater;

    public OrderAdapter(List<Order> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

    }

    @Override

    public OrderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_order_list, parent, false);

        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderItemViewHolder holder, int position) {
        Order order = mDatas.get(position);

        Picasso.with(mContext)
                .load(Config.baseUrl+order.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);
        if (order.getPs().size() > 0) {
            holder.mTvLabel.setText(order.getPs().get(0).product.getName() + "等" + order.getCount() + "件商品");
        } else {
            holder.mTvLabel.setText("无消费");
        }
        holder.mTvName.setText(order.getRestaurant().getName());
        holder.mTvPrice.setText("共消费: "+order.getPrice()+"元");
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvImage;
        public TextView mTvName;
        public TextView mTvLabel;
        public TextView mTvPrice;
        public OrderItemViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderDetailActivity.launch(mContext,mDatas.get(getLayoutPosition()));
                }
            });

            mIvImage = itemView.findViewById(R.id.id_iv_image);
            mTvName = itemView.findViewById(R.id.id_tv_name);
            mTvLabel = itemView.findViewById(R.id.id_tv_label);
            mTvPrice = itemView.findViewById(R.id.id_tv_price);
        }
    }
}

