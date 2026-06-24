package de.hs_kl.rateme.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;


@Entity
@Table(name = "user")
@NamedQueries(
        {
                @NamedQuery(name = "findUserByUsername",
                        query = "SELECT u FROM User u WHERE u.username = :username")
        ,

        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
private int id;
    @Column(unique = true,length = 20,nullable = false)
private String username;
    @Column(nullable = false, length = 50)
    @NotBlank(message = "Email is required")
private String email;

    @Column(nullable = false, length = 20)

    private String firstname;

    @Column(nullable = false, length = 20)

    private String lastname;

    @Column(nullable = false, length = 30)

    private String street;

    @Column(name = "street_nr",nullable = false, length = 20)

    private String streetNr;

    @Column(nullable = false, length = 20)

    private String zip;

    @Column(nullable = false, length = 30)

    private String city;
@Column(name = "password_hash",nullable = false,length = 1000)
private byte[] passwordHash;

    @Column(name = "password_salt",nullable = false,length = 1000)
private byte[] passwordSalt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId() && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getFirstname(), user.getFirstname()) && Objects.equals(getLastname(), user.getLastname()) && Objects.equals(getStreet(), user.getStreet()) && Objects.equals(getStreetNr(), user.getStreetNr()) && Objects.equals(getZip(), user.getZip()) && Objects.equals(getCity(), user.getCity()) && Objects.deepEquals(getPasswordHash(), user.getPasswordHash()) && Objects.deepEquals(getPasswordSalt(), user.getPasswordSalt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getEmail(), getFirstname(), getLastname(), getStreet(), getStreetNr(), getZip(), getCity(), Arrays.hashCode(getPasswordHash()), Arrays.hashCode(getPasswordSalt()));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNr() {
        return streetNr;
    }

    public void setStreetNr(String streetNr) {
        this.streetNr = streetNr;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }
    @OneToMany(mappedBy = "user")
    private Collection<Rating> ratingsUser = new ArrayList<>();

    public Collection<Rating> getRatingsUser() {
        return ratingsUser;
    }
}
