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


// 122337932	Nguyen	2000-11-19	1	ninhnguyendx779@gmail.com	ninh2000	Ninh Nguyen	+84586433967	79

@Entity
public class Useraccount {
    private int id;
    private String name;
    private String birth;
    private int gender;
    private String email;
    private String pass;
    private String fullname;
    private String phonenumber;
    private int address;
    private int active;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 50)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 50)
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "pass", nullable = false, length = 30)
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }

    @Basic
    @Column(name = "birth", nullable = true, length = 10)
    public String getBirth() {
        return birth;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }

    @Basic
    @Column(name = "gender", nullable = true)
    public int getGender() {
        return gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "fullname", nullable = true, length = 50)
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Basic
    @Column(name = "phonenumber", nullable = true, length = 20)
    public String getPhonenumber() {
        return phonenumber;
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @Basic
    @Column(name = "address", nullable = false, length = 100)
    public int getAddress() {
        return address;
    }
    public void setAddress(int address) {
        this.address = address;
    }

    public int getActive() {
        return active;
    }
    public void setActive(int active) {
        this.active = active;
    }
}
