package hcmute.edu.vn.mssv18110332.model;

import androidx.room.Entity;

import hcmute.edu.vn.mssv18110332.DAO.UserAccountDAO;
import hcmute.edu.vn.mssv18110332.interface_define.Basic;
import hcmute.edu.vn.mssv18110332.interface_define.Column;
import hcmute.edu.vn.mssv18110332.interface_define.Id;

@Entity
public class Address {
    private int id;
    private int user;
    private String name;
    private int pro;
    private int dis;
    private int war;
    private String home;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user", nullable = false)
    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getName(){return this.name;}
    public void setName(String value) {this.name = value;}

    @Basic
    @Column(name = "pro", nullable = false)
    public int getPro() {
        return pro;
    }

    public void setPro(int pro) {
        this.pro = pro;
    }

    @Basic
    @Column(name = "dis", nullable = false)
    public int getDis() {
        return dis;
    }

    public void setDis(int dis) {
        this.dis = dis;
    }

    @Basic
    @Column(name = "war", nullable = false)
    public int getWar() {
        return war;
    }

    public void setWar(int war) {
        this.war = war;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (id != address.id) return false;
        if (user != address.user) return false;
        if (pro != address.pro) return false;
        if (dis != address.dis) return false;
        if (war != address.war) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + user;
        result = 31 * result + pro;
        result = 31 * result + dis;
        result = 31 * result + war;
        return result;
    }
}
