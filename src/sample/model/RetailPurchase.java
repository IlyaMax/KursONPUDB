package sample.model;

import java.sql.Date;

public class RetailPurchase {
    private Integer id;
    private Date date;
    private Integer quantity;
    private Integer price;
    private Reagent reagent;

    public RetailPurchase(Integer id, Date date, Integer quantity, Integer price, Reagent reagent) {
        this.id = id;
        this.date = date;
        this.quantity = quantity;
        this.price = price;
        this.reagent = reagent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Reagent getReagent() {
        return reagent;
    }

    public void setReagent(Reagent reagent) {
        this.reagent = reagent;
    }


}
