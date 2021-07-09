package hcmute.edu.vn.mssv18110332.model;

import androidx.room.Entity;

import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.interface_define.Basic;
import hcmute.edu.vn.mssv18110332.interface_define.Column;
import hcmute.edu.vn.mssv18110332.interface_define.Id;


@Entity
public class Orders {
    private int id;
    private int uid;
    private String uname;
    private String uphone;
    private String uaddress;
    private String start_date;
    private String end_date;
    private int pid;
    private String state;
    private int did;

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public Orders() {
        this.id = 0;
        this.uid = 0;
        this.uname = "Đặt hàng online qua app Ministop";
        this.uphone = "";
        this.uaddress = "uaddress";
        this.start_date = AppUtils.getCurrentDateTime();
        this.end_date = "endDate";
        this.pid = 0;
        this.state = "Created";
        this.did = 1;
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
    @Column(name = "uid", nullable = false)
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Basic
    @Column(name = "uname", nullable = false, length = 50)
    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Basic
    @Column(name = "uphone", nullable = false, length = 12)
    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    @Basic
    @Column(name = "uaddress", nullable = false, length = 500)
    public String getUaddress() {
        return uaddress;
    }

    public void setUaddress(String uaddress) {
        this.uaddress = uaddress;
    }

    @Basic
    @Column(name = "start_date", nullable = false, length = 10)
    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    @Basic
    @Column(name = "end_date", nullable = false, length = 10)
    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    @Basic
    @Column(name = "pid", nullable = false)
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "state", nullable = false, length = 50)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Orders orders = (Orders) o;

        if (id != orders.id) return false;
        if (uid != orders.uid) return false;
        if (pid != orders.pid) return false;
        if (uname != null ? !uname.equals(orders.uname) : orders.uname != null) return false;
        if (uphone != null ? !uphone.equals(orders.uphone) : orders.uphone != null) return false;
        if (uaddress != null ? !uaddress.equals(orders.uaddress) : orders.uaddress != null) return false;
        if (start_date != null ? !start_date.equals(orders.start_date) : orders.start_date != null) return false;
        if (end_date != null ? !end_date.equals(orders.end_date) : orders.end_date != null) return false;
        if (state != null ? !state.equals(orders.state) : orders.state != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + uid;
        result = 31 * result + (uname != null ? uname.hashCode() : 0);
        result = 31 * result + (uphone != null ? uphone.hashCode() : 0);
        result = 31 * result + (uaddress != null ? uaddress.hashCode() : 0);
        result = 31 * result + (start_date != null ? start_date.hashCode() : 0);
        result = 31 * result + (end_date != null ? end_date.hashCode() : 0);
        result = 31 * result + pid;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }
}
