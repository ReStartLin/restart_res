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
import restart.com.restart_res.config.Config;
import restart.com.restart_res.ui.vo.ProductItem;
import restart.com.restart_res.utils.T;

/**
 * Created by Administrator on 2018/3/30.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListItemViewHolder>{
    private Context mContext;
    private List<ProductItem> mProductItems;
    private LayoutInflater inflater;

    public ProductListAdapter(Context mContext, List<ProductItem> mProductItems) {
        this.mContext = mContext;
        this.mProductItems = mProductItems;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ProductListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_product_list, parent, false);
        return new ProductListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductListItemViewHolder holder, int position) {
        ProductItem productItem = mProductItems.get(position);

        Picasso.with(mContext)
                .load(Config.baseUrl+productItem.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);
        holder.mTvName.setText(productItem.getName());
        holder.mTvCount.setText(productItem.getCount()+"");
        holder.mTvLabel.setText(productItem.getLabel());
        holder.mTvPrice.setText(productItem.getPrice()+"元一份");


    }

    @Override
    public int getItemCount() {
        return mProductItems.size();
    }

    private OnProductListener onProductListener;

    public void setOnProductListener(OnProductListener onProductListener) {
        this.onProductListener = onProductListener;
    }
    public interface OnProductListener{
        void onProductAdd(ProductItem productItem);
        void onProductSub(ProductItem productItem);
    }
    class ProductListItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIvImage;
        public TextView mTvName;
        public TextView mTvLabel;
        public TextView mTvPrice;

        public ImageView mIvAdd;
        public ImageView mIvSub;
        public TextView mTvCount;

        public ProductListItemViewHolder(View itemView) {
            super(itemView);

            mIvImage = itemView.findViewById(R.id.id_iv_image);
            mTvName = itemView.findViewById(R.id.id_tv_name);
            mTvLabel = itemView.findViewById(R.id.id_tv_label);
            mTvPrice = itemView.findViewById(R.id.id_tv_price);
            mIvAdd = itemView.findViewById(R.id.id_iv_add);
            mIvSub = itemView.findViewById(R.id.id_iv_sub);
            mTvCount = itemView.findViewById(R.id.id_tv_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 商品详情
                }
            });

            mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = mProductItems.get(pos);
                    productItem.setCount(productItem.getCount()+1);
                    mTvCount.setText(productItem.getCount()+"");
                    if (onProductListener != null) {
                        onProductListener.onProductAdd(productItem);
                    }
                }
            });
            mIvSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = mProductItems.get(pos);
                    if (productItem.getCount() <= 0) {
                        T.showToast("已经是0了");
                        return;
                    }
                    productItem.setCount(productItem.getCount()-1);
                    mTvCount.setText(productItem.getCount()+"");
                    if (onProductListener != null) {
                        onProductListener.onProductSub(productItem);
                    }
                }
            });
        }
    }

}
