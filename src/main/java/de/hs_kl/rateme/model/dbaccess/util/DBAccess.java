package de.hs_kl.rateme.model.dbaccess.util;

import de.hs_kl.rateme.entity.Image;
import de.hs_kl.rateme.entity.Poi;
import de.hs_kl.rateme.entity.Rating;
import de.hs_kl.rateme.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.Collection;

@Service
@Transactional
public class DBAccess {
    private final EntityManager entityManager;
    @Autowired
    public DBAccess(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    // ============User============
    public User createUser(String username ,String password,String email
            , String firstname,String lastname
    ,String street,String street_nr,String zip, String city){
    User user = new User();

    try {
        findUserByUsername(username);
        throw new ResponseStatusException(HttpStatus.CONFLICT , "User already exists");
    }catch (NoResultException ignored) {

    }
        byte[] salt = PasswordTools.generateSalt();
        byte[] hash = PasswordTools.generatePasswordHash(password, salt);
        user.setUsername(username);
        user.setPasswordSalt(salt);
        user.setPasswordHash(hash);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setStreet(street);
        user.setStreetNr(street_nr);
        user.setZip(zip);
        user.setCity(city);
        entityManager.persist(user);
        entityManager.flush();
        entityManager.refresh(user);
        return user;
    }
    public User findUserById(int id){
     return entityManager.find(User.class,id);
    }

    public User findUserByUsername(String username){
    return entityManager
            .createNamedQuery("findUserByUsername",User.class)
            .setParameter("username", username)
            .getSingleResult();
 }
    //============Poi============
    public Collection<Poi> getAllPois(){
        return entityManager.createNamedQuery("getAllPois", Poi.class).getResultList();
    }
    public Poi getPoiById(long id){
        return entityManager.find(Poi.class, id);
    }
    //============Image============
    public Image createImg(byte[] img){
        Image image = new Image();
        image.setImg(img);

        entityManager.persist(image);
        entityManager.flush();
        entityManager.refresh(image);
        return image;
    }
    public Image findImgById(Integer id){
        return entityManager.find(Image.class,id);
    }
    //============Rating============
    public Rating createRating(int userId , long poiId, int grade, String txt, Integer image_id){
        Rating rating = new Rating();

        if (grade < 0 || grade > 5) {
            throw new IllegalArgumentException("Grade must be between 0 and 5");
        }
        User user = findUserById(userId);
        Poi poi = getPoiById(poiId);
        Image image = (image_id != null) ? findImgById(image_id) : null;

        rating.setUser(user);
        rating.setPoi(poi);
        rating.setImage(image);
        rating.setGrade(grade);
        rating.setTxt(txt);

        entityManager.persist(rating);
        entityManager.flush();
        entityManager.refresh(rating);

        return rating;

    }
    public Collection<Rating> findRatingsByPoiId(long poiId){
        return entityManager
                .createNamedQuery("findRatingsByPoiId",Rating.class)
                .setParameter("poiId",poiId).getResultList();
    }
    public Collection<Rating> findRatingByUserId(int userId){
        return entityManager
                .createNamedQuery("findRatingByUserId",Rating.class)
                .setParameter("userId",userId).getResultList();
    }
}
