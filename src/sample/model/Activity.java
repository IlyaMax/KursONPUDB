package sample.model;

import com.sun.istack.internal.Nullable;

import java.sql.Time;

public class Activity {
    private Integer id;
    private String name;
    private Time startTime;
    @Nullable
    private Time doneTime;
    private Order order;
    private Employee employee;

    public Activity(Integer id, String name, Time startTime, Time doneTime, Order order, Employee employee) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.doneTime = doneTime;
        this.order = order;
        this.employee = employee;
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

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(Time doneTime) {
        this.doneTime = doneTime;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
