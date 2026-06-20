package de.hs_kl.rateme.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "image")
public class Image {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(name = "img", nullable = false)
    @Lob
    private byte[] img;

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return getId() == image.getId() && Objects.deepEquals(getImg(), image.getImg());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), Arrays.hashCode(getImg()));
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }


}
