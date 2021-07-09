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
import hcmute.edu.vn.mssv18110332.interface_define.IdClass;
import hcmute.edu.vn.mssv18110332.interface_define.JoinColumn;
import hcmute.edu.vn.mssv18110332.interface_define.ManyToOne;
import hcmute.edu.vn.mssv18110332.interface_define.OneToMany;
import me.relex.circleindicator.CircleIndicator;

@Entity
public class Wards {
    private int id;
    private String name;
    private int districtId;
    private String level;
    private Districts districtsByDistrictId;

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
    @Column(name = "district_id", nullable = false)
    public Object getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    @Basic
    @Column(name = "level", nullable = false, length = 255)
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wards wards = (Wards) o;

        if (id != wards.id) return false;
        if (name != null ? !name.equals(wards.name) : wards.name != null) return false;
        if (districtId != wards.districtId) return false;
        if (level != null ? !level.equals(wards.level) : wards.level != null) return false;

        return true;
    }
    @ManyToOne
    @JoinColumn(name = "district_id", referencedColumnName = "id", nullable = false)
    public Districts getDistrictsByDistrictId() {
        return districtsByDistrictId;
    }

    public void setDistrictsByDistrictId(Districts districtsByDistrictId) {
        this.districtsByDistrictId = districtsByDistrictId;
    }
}
