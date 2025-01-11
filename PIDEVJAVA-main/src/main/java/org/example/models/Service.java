package org.example.models;

public class Service {
    private Integer id;
    private String name;
    private Float price;
    private Question question;

    public Service(String name,Float price) {
        this.name = name;
        this.price = price;
    }

    public Service(int serviceId, String serviceName, Float servicePrice) {
        this.id = serviceId;
        this.name = serviceName;
        this.price = servicePrice;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}