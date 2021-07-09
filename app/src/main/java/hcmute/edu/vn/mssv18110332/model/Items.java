package hcmute.edu.vn.mssv18110332.model;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Entity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.adapter.viewfliper.Photo;
import hcmute.edu.vn.mssv18110332.adapter.viewfliper.PhotoAdapter;
import hcmute.edu.vn.mssv18110332.databinding.FragmentHomeBinding;
import hcmute.edu.vn.mssv18110332.interface_define.Basic;
import hcmute.edu.vn.mssv18110332.interface_define.Column;
import hcmute.edu.vn.mssv18110332.interface_define.Id;
import hcmute.edu.vn.mssv18110332.interface_define.JoinColumn;
import hcmute.edu.vn.mssv18110332.interface_define.ManyToOne;
import hcmute.edu.vn.mssv18110332.interface_define.OneToMany;
import me.relex.circleindicator.CircleIndicator;


@Entity
public class Items {
    private int id;
    private String name;
    private String image;
    private int price;
    private int category;
    private int stock;
    private String provider;
    private String mfg;
    private String exp;
    private Category categoryByCategory;
    private int discounted;
    private String promotion;
    private String delivery;
    private int active;

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Name", nullable = false, length = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "Image", nullable = false, length = 500)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Basic
    @Column(name = "Price", nullable = false, precision = 0)
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    @Basic
    @Column(name = "Category", nullable = false)
    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Basic
    @Column(name = "Stock", nullable = false)
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Basic
    @Column(name = "Provider", nullable = false, length = 20)
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Basic
    @Column(name = "MFG", nullable = false, length = 20)
    public String getMfg() {
        return mfg;
    }

    public void setMfg(String mfg) {
        this.mfg = mfg;
    }

    @Basic
    @Column(name = "EXP", nullable = false, length = 10)
    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public int getDiscounted() {
        return this.discounted;
    }
    public void setDiscounted(int discounted)
    {
        this.discounted = discounted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Items items = (Items) o;

        if (id != items.id) return false;
        if (Double.compare(items.price, price) != 0) return false;
        if (category != items.category) return false;
        if (stock != items.stock) return false;
        if (name != null ? !name.equals(items.name) : items.name != null) return false;
        if (provider != null ? !provider.equals(items.provider) : items.provider != null) return false;
        if (mfg != null ? !mfg.equals(items.mfg) : items.mfg != null) return false;
        if (exp != null ? !exp.equals(items.exp) : items.exp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + category;
        result = 31 * result + stock;
        result = 31 * result + (provider != null ? provider.hashCode() : 0);
        result = 31 * result + (mfg != null ? mfg.hashCode() : 0);
        result = 31 * result + (exp != null ? exp.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "Category", referencedColumnName = "id", nullable = false)
    public Category getCategoryByCategory() {
        return categoryByCategory;
    }

    public void setCategoryByCategory(Category categoryByCategory) {
        this.categoryByCategory = categoryByCategory;
    }
}
