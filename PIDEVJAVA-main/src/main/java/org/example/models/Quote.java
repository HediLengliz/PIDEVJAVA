package org.example.models;

import java.util.List;

public class Quote {
    private Integer id;
    private String type;
    private Float amount;
    private User user;
    private List<String> services;

    public Quote(String type,Float amount,User user){
        this.type =type;
        this.amount = amount;
        this.user = user;

    }

    public Quote(Integer id,String type,Float amount,User user){
        this.id = id;
        this.type =type;
        this.amount = amount;
        this.user = user;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", user=" + user +
                ", services=" + services +
                '}';
    }
}