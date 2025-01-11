package org.example.models;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private Integer id;
    private String question;
    private String type;
    private List<Service> services;
    private Integer priority;

    public Question(String question,String type,Integer priority) {
        this.question = question;
        this.type = type;
        this.priority = priority;
        this.services = new ArrayList<>();
    }
    public Question(int id,String question,String type,Integer priority) {
        this.id = id;
        this.question = question;
        this.type = type;
        this.priority = priority;
        this.services = new ArrayList<>();
    }

    public Question() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Service> getServices() {
        return services;
    }

    public void addService(Service service) {
        if (!this.services.contains(service)) {
            this.services.add(service);
            service.setQuestion(this);
        }
    }

    public void removeService(Service service) {
        if (this.services.remove(service)) {
            if (service.getQuestion() == this) {
                service.setQuestion(null);
            }
        }
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
