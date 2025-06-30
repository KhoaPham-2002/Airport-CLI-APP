//Name: Khoa Pham
//Project: Midterm Sprint (Airport-CLI-App)
//Date: 06/29/2025

package com.khoa.airportcli.domain;

public class Airport {
    private Long id;
    private String name;
    private String code;
    private City city;

    public Airport() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", city=" + city +
                '}';
    }
}

