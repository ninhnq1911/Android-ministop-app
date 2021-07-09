package hcmute.edu.vn.mssv18110332.model;

import androidx.room.Entity;

import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.interface_define.Basic;
import hcmute.edu.vn.mssv18110332.interface_define.Column;
import hcmute.edu.vn.mssv18110332.interface_define.Id;

@Entity
public class Payment {
    private int pid;
    private String name;
    private int uid;
    private int amount;
    private String uname;
    private String uphone;
    private String uaddress;
    private int method;
    private String date;
    private String status;

    public Payment()
    {
        this.pid = 0;
        this.date = AppUtils.getCurrentDateTime();
        this.status = "Created";
        this.name = "Thanh toán hàng hóa / dịch vụ MiniStop";
        this.amount = 0;
        this.uid = 0;
        this.uname = "user";
        this.uphone = "";
        this.uaddress = "";
        this.method = 1;
    }

    @Basic
    @Column(name = "amount", nullable = false, precision = 0)
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "date", nullable = false, length = 10)
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        if (Double.compare(payment.amount, amount) != 0) return false;
        if (name != null ? !name.equals(payment.name) : payment.name != null) return false;
        if (date != null ? !date.equals(payment.date) : payment.date != null) return false;

        return true;
    }

    @Id
    @Column(name = "pid", nullable = false)
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
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
    @Column(name = "mehtod", nullable = false, length = 50)
    public int getMethod() {
        return method;
    }
    public void setMethod(int method) {
        this.method = method;
    }

    @Basic
    @Column(name = "status", nullable = false, length = 50)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
