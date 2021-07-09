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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.adapter.viewfliper.Photo;
import hcmute.edu.vn.mssv18110332.adapter.viewfliper.PhotoAdapter;
import hcmute.edu.vn.mssv18110332.databinding.FragmentHomeBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.interface_define.Basic;
import hcmute.edu.vn.mssv18110332.interface_define.Column;
import hcmute.edu.vn.mssv18110332.interface_define.Id;
import me.relex.circleindicator.CircleIndicator;

@Entity
public class Cart {
    private int user;
    private int id;
    private int item;
    private int sl;
    private int check;

    public Cart()
    {
        setId(0);
        setCheck(0);
        setItem(19);
        setSl(1);
        setUser(AppUtils.getCurrentUserID());
    }
    public int getCheck() {
        return check;
    }
    public void setCheck(int check) {
        this.check = check;
    }
    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "item", nullable = false)
    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cart cart = (Cart) o;

        if (id != cart.id) return false;
        if (item != cart.item) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + item;
        return result;
    }

    @Basic
    @Column(name = "user", nullable = false)
    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    @Basic
    @Column(name = "sl", nullable = false)
    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }
}
