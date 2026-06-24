package de.hs_kl.rateme.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "rating")
@NamedQueries(
        {
        @NamedQuery(name = "getAllRatings",
        query = "SELECT r FROM Rating r"),
        @NamedQuery(name = "findRatingsByPoiId",
        query = "SELECT r FROM Rating r WHERE r.poi.id= :poiId"),
        @NamedQuery(name = "findRatingByUserId",
        query = "SELECT r FROM Rating r WHERE r.user.id = :userId"),

        }

)
public class Rating {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   private int grade;
   private Timestamp created_at = new Timestamp(System.currentTimeMillis());
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return getId() == rating.getId() && getGrade() == rating.getGrade() && Objects.equals(getCreated_at(), rating.getCreated_at()) && Objects.equals(getTxt(), rating.getTxt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGrade(), getCreated_at(), getTxt());
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }


    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(length = 1000)
   private String txt;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "poi_id",nullable = false)
    private Poi poi;
    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image   image;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Poi getPoi() {
        return poi;
    }

    public void setPoi(Poi poi) {
        this.poi = poi;
    }
}
