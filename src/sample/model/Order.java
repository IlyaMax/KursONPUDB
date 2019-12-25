package sample.model;

import java.util.List;

public class Order {
    private Integer id;
    private String description;
    private Client client;

    public Order(Integer id, String description, Client client) {
        this.id = id;
        this.description = description;
        this.client = client;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
