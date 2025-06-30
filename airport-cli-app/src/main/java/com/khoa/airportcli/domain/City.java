//Name: Khoa Pham
//Project: Midterm Sprint (Airport-CLI-App)
//Date: 06/29/2025

package com.khoa.airportcli.domain;

public class City {
    private long id;
    private String name;
    private String state;
    private long population;

    public City() {
    }

    public City(long id, String name, String state, long population) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.population = population;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", population=" + population +
                '}';
    }
}

