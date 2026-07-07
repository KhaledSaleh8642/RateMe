package de.hs_kl.rateme.api.Controllers;

import de.hs_kl.rateme.api.DTOs.ImageDtoOut;
import de.hs_kl.rateme.entity.Image;
import de.hs_kl.rateme.model.dbaccess.util.DBAccess;
import de.hs_kl.rateme.security.SecurityManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/images")
public class ImageController {
    private final DBAccess dbAccess;
    private final SecurityManager securityManager;

    public ImageController(DBAccess dbAccess, SecurityManager securityManager) {
        this.dbAccess = dbAccess;
        this.securityManager = securityManager;
    }
    @PostMapping
    public ResponseEntity<ImageDtoOut> uploadImg(@RequestParam ("file") MultipartFile file,
                                                 @RequestHeader ("Authorization") String token){
        securityManager.checkIfTokenIsAccepted(token);
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Image image = dbAccess.createImg(file.getBytes());
            ImageDtoOut out = new ImageDtoOut(image.getId());

            return ResponseEntity.ok(out);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<byte[]> getImg(@PathVariable int id){
        Image image = dbAccess.findImgById(id);

        if(image == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(image.getImg());

    }
}
