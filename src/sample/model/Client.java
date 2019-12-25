package sample.model;

import java.util.List;

public class Client {
    private Integer id;
    private String name;
    private String phone;
    private String info;

    public Client(Integer id, String name, String phone, String info) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.info = info;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
