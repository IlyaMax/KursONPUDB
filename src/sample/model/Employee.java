package sample.model;

import com.sun.istack.internal.Nullable;

import java.sql.Date;
import java.util.List;

public class Employee {
    private Integer id;
    private String name;
    private Date placementDate;
    @Nullable
    private Date dismissalDate;
    private String position;
    private Integer salary;

    public Employee(Integer id, String name, Date placementDate, Date dismissalDate, String position, Integer salary) {
        this.id = id;
        this.name = name;
        this.placementDate = placementDate;
        this.dismissalDate = dismissalDate;
        this.position = position;
        this.salary = salary;
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

    public Date getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(Date placementDate) {
        this.placementDate = placementDate;
    }

    public Date getDismissalDate() {
        return dismissalDate;
    }

    public void setDismissalDate(Date dismissalDate) {
        this.dismissalDate = dismissalDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }
}
