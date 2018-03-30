package restart.com.restart_res.ui.vo;


import restart.com.restart_res.bean.Product;

/**
 * Created by zhanghongyang01 on 16/10/18.
 */

public class ProductItem extends Product {

//    public ProductItem(String name, String label, String desc, float price, String url) {
//        super(name, label, desc, price, url);
//    }

    public ProductItem(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.label = product.getLabel();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.icon = product.getIcon();
        this.restaurant = product.getRestaurant();
    }

    public ProductItem() {
    }

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
