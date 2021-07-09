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
import hcmute.edu.vn.mssv18110332.interface_define.OneToMany;
import me.relex.circleindicator.CircleIndicator;

@Entity
public class Category {
    private int id;
    private String name;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (id != category.id) return false;
        if (name != null ? !name.equals(category.name) : category.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
