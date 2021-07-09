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
import hcmute.edu.vn.mssv18110332.interface_define.JoinColumn;
import hcmute.edu.vn.mssv18110332.interface_define.ManyToOne;
import hcmute.edu.vn.mssv18110332.interface_define.OneToMany;
import me.relex.circleindicator.CircleIndicator;

@Entity
public class Districts {
    private int id;
    private String name;
    private int provinceId;
    private Provinces provincesByProvinceId;
    private Collection<Wards> wardsById;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "province_id", nullable = false)
    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Districts districts = (Districts) o;

        if (id != districts.id) return false;
        if (name != null ? !name.equals(districts.name) : districts.name != null) return false;
        if (provinceId != districts.provinceId) return false;

        return true;
    }

    @ManyToOne
    @JoinColumn(name = "province_id", referencedColumnName = "id", nullable = false)
    public Provinces getProvincesByProvinceId() {
        return provincesByProvinceId;
    }

    public void setProvincesByProvinceId(Provinces provincesByProvinceId) {
        this.provincesByProvinceId = provincesByProvinceId;
    }

    @OneToMany(mappedBy = "districtsByDistrictId")
    public Collection<Wards> getWardsById() {
        return wardsById;
    }

    public void setWardsById(Collection<Wards> wardsById) {
        this.wardsById = wardsById;
    }
}
