package de.hs_kl.rateme.entity;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Objects;


    @Entity
    @Table(name = "poi")
    @NamedQueries({
            @NamedQuery(name = "getAllPois",
            query = "SELECT p FROM Poi p" )
    })
public class Poi {
    @Id
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "type",nullable = false)
    private poiType type;

private Double lat;
private Double lon;
private String name;
private String amenity;
private String cuisine;
private String phone;


    @Column(name = "opening_hours")
private String openingHours;
private String website;
private String wheelchair;
@Column(name = "takeaway")
private String takeAway;
private String delivery;
private String smoking;
@Column(name = "outdoor_seating")
private String outdoorSeating;
private String reservation;
@Column(name = "addr_city")
private String addrCity;
@Column(name = "addr_country")
private String addrCountry;
@Column(name = "addr_housenumber")
private String addrHouseNumber;
@Column(name = "addr_postcode")
private String addrPostCode;
@Column(name = "addr_street")
private String addrStreet;
    @Column(nullable = false, columnDefinition = "JSON")
private String tags;
    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public poiType getType() {
        return type;
    }

    public void setType(poiType type) {
        this.type = type;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWheelchair() {
        return wheelchair;
    }

    public void setWheelchair(String wheelchair) {
        this.wheelchair = wheelchair;
    }

    public String getTakeAway() {
        return takeAway;
    }

    public void setTakeAway(String takeAway) {
        this.takeAway = takeAway;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getOutdoorSeating() {
        return outdoorSeating;
    }

    public void setOutdoorSeating(String outdoorSeating) {
        this.outdoorSeating = outdoorSeating;
    }

    public String getReservation() {
        return reservation;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
    }

    public String getAddrCity() {
        return addrCity;
    }

    public void setAddrCity(String addrCity) {
        this.addrCity = addrCity;
    }

    public String getAddrCountry() {
        return addrCountry;
    }

    public void setAddrCountry(String addrCountry) {
        this.addrCountry = addrCountry;
    }

    public String getAddrHouseNumber() {
        return addrHouseNumber;
    }

    public void setAddrHouseNumber(String addrHouseNumber) {
        this.addrHouseNumber = addrHouseNumber;
    }

    public String getAddrPostCode() {
        return addrPostCode;
    }

    public void setAddrPostCode(String addrPostCode) {
        this.addrPostCode = addrPostCode;
    }

    public String getAddrStreet() {
        return addrStreet;
    }

    public void setAddrStreet(String addrStreet) {
        this.addrStreet = addrStreet;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Poi poi = (Poi) o;
        return getId() == poi.getId() && getType() == poi.getType() && Objects.equals(getLat(), poi.getLat()) && Objects.equals(getLon(), poi.getLon()) && Objects.equals(getName(), poi.getName()) && Objects.equals(getAmenity(), poi.getAmenity()) && Objects.equals(getCuisine(), poi.getCuisine()) && Objects.equals(getPhone(), poi.getPhone()) && Objects.equals(getOpeningHours(), poi.getOpeningHours()) && Objects.equals(getWebsite(), poi.getWebsite()) && Objects.equals(getWheelchair(), poi.getWheelchair()) && Objects.equals(getTakeAway(), poi.getTakeAway()) && Objects.equals(getDelivery(), poi.getDelivery()) && Objects.equals(getSmoking(), poi.getSmoking()) && Objects.equals(getOutdoorSeating(), poi.getOutdoorSeating()) && Objects.equals(getReservation(), poi.getReservation()) && Objects.equals(getAddrCity(), poi.getAddrCity()) && Objects.equals(getAddrCountry(), poi.getAddrCountry()) && Objects.equals(getAddrHouseNumber(), poi.getAddrHouseNumber()) && Objects.equals(getAddrPostCode(), poi.getAddrPostCode()) && Objects.equals(getAddrStreet(), poi.getAddrStreet()) && Objects.equals(getTags(), poi.getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getType(), getLat(), getLon(), getName(), getAmenity(), getCuisine(), getPhone(), getOpeningHours(), getWebsite(), getWheelchair(), getTakeAway(), getDelivery(), getSmoking(), getOutdoorSeating(), getReservation(), getAddrCity(), getAddrCountry(), getAddrHouseNumber(), getAddrPostCode(), getAddrStreet(), getTags());
    }
    @OneToMany(mappedBy = "poi")
    private Collection<Rating> ratingsPoi;

    public Collection<Rating> getRatingsPoi() {
        return ratingsPoi;
    }
}
