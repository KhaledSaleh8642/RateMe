package de.hs_kl.rateme.api.controllers;

import de.hs_kl.rateme.api.dtos.PoiDtoOut;
import de.hs_kl.rateme.entity.Poi;
import de.hs_kl.rateme.model.dbaccess.util.DBAccess;
import de.hs_kl.rateme.security.SecurityManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("api/pois")
public class PoiController {
    private final DBAccess dbAccess;
    private final SecurityManager securityManager;

    public PoiController(DBAccess dbAccess, SecurityManager securityManager) {
        this.dbAccess = dbAccess;
        this.securityManager = securityManager;
    }
    @GetMapping
    public ResponseEntity<Collection<PoiDtoOut>> getAllPois(@RequestHeader ("Authorization") String token ){
        securityManager.checkIfTokenIsAccepted(token);
        Collection<Poi> pois = dbAccess.getAllPois();
        Collection<PoiDtoOut> result = new ArrayList<>();
      for(var x : pois){
          PoiDtoOut out = new PoiDtoOut(x.getId(),x.getLat(),x.getLon(), x.getName(), x.getPhone(), x.getAmenity());
          result.add(out);
      }
        return ResponseEntity.ok(result);

    }
    @GetMapping("{id}")
    public ResponseEntity<PoiDtoOut> getPoiById(@PathVariable long id, @RequestHeader ("Authorization") String token){
        securityManager.checkIfTokenIsAccepted(token);
        Poi poi = dbAccess.getPoiById(id);
        PoiDtoOut out = new PoiDtoOut(poi.getId(),poi.getLat(), poi.getLon(), poi.getName(), poi.getPhone(), poi.getAmenity());

        return ResponseEntity.ok(out);
    }
}
