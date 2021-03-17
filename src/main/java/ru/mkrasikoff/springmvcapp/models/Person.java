package ru.mkrasikoff.springmvcapp.models;

public class Person {
    private int id;
    private String name;
    private String subname;
    private String email;

    public Person(int id, String name, String subname, String email) {
        this.id = id;
        this.name = name;
        this.subname = subname;
        this.email = email;
    }

    public Person() {
    }

    public int getId() {
        return id;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
