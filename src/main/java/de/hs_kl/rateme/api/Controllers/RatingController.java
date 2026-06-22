package de.hs_kl.rateme.api.Controllers;

import de.hs_kl.rateme.api.DTOs.RatingDtoIn;
import de.hs_kl.rateme.api.DTOs.RatingDtoOut;
import de.hs_kl.rateme.entity.Rating;
import de.hs_kl.rateme.model.dbaccess.util.DBAccess;
import de.hs_kl.rateme.security.SecurityManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("api/ratings")
public class RatingController {
    private final DBAccess dbAccess;
    private final SecurityManager securityManager;

    public RatingController(DBAccess dbAccess, SecurityManager securityManager) {
        this.dbAccess = dbAccess;
        this.securityManager = securityManager;
    }
    private RatingDtoOut toDtoOut(Rating rating){

        Integer imageId = rating.getImage() == null ? null : rating.getImage().getId() ;

        return new RatingDtoOut(rating.getId(),rating.getCreated_at(),rating.getUser().getUsername(),rating.getPoi().getName(),
                rating.getTxt(), rating.getGrade(), imageId);
    }





    @PostMapping
    public ResponseEntity<RatingDtoOut> rate(@RequestBody RatingDtoIn dtoIn, @RequestHeader("Authorization") String token) {

        securityManager.checkIfTokenIsAccepted(token);
        Rating rating = dbAccess.createRating(securityManager.getUser(token).getId(), dtoIn.poiId(),dtoIn.grade(),dtoIn.txt(), dtoIn.imageId());



        return ResponseEntity.ok(toDtoOut(rating));

    }
    @GetMapping("/me")
    public ResponseEntity<Collection<RatingDtoOut>> myRatings(@RequestHeader ("Authorization") String token){
        securityManager.checkIfTokenIsAccepted(token);
    int userId = securityManager.getUser(token).getId();
    Collection<Rating> ratings = dbAccess.findRatingByUserId(userId);
    Collection<RatingDtoOut> ratingDtoOuts = new ArrayList<>();
    for(var rating : ratings){
        RatingDtoOut dtoOut = toDtoOut(rating);
        ratingDtoOuts.add(dtoOut);
    }
    return ResponseEntity.ok(ratingDtoOuts);
    }



    @GetMapping("/poi/{poiId}")
    public ResponseEntity<Collection<RatingDtoOut>> findRatingsByPoiId(@PathVariable long poiId, @RequestHeader ("Authorization") String token){
        securityManager.checkIfTokenIsAccepted(token);
        Collection<Rating> ratings = dbAccess.findRatingsByPoiId(poiId);
        Collection<RatingDtoOut> ratingDtoOuts = new ArrayList<>();

        for(var rating : ratings){

            RatingDtoOut dtoOut = toDtoOut(rating);
            ratingDtoOuts.add(dtoOut);
        }
        return ResponseEntity.ok(ratingDtoOuts);
    }

}
