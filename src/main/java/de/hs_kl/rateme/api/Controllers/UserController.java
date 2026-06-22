package de.hs_kl.rateme.api.Controllers;

import de.hs_kl.rateme.api.DTOs.LogInDtoIn;
import de.hs_kl.rateme.api.DTOs.RegisterDtoIn;
import de.hs_kl.rateme.api.DTOs.UserDtoOut;
import de.hs_kl.rateme.entity.User;
import de.hs_kl.rateme.model.dbaccess.util.DBAccess;
import de.hs_kl.rateme.model.dbaccess.util.PasswordTools;
import de.hs_kl.rateme.security.SecurityManager;
import jakarta.persistence.NoResultException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final SecurityManager securityManager;
    private final DBAccess dbAccess;

    public UserController(SecurityManager securityManager, DBAccess dbAccess) {
        this.securityManager = securityManager;
        this.dbAccess = dbAccess;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDtoOut> register(@RequestBody RegisterDtoIn dtoIn) {
        User user = dbAccess.createUser(dtoIn.username(), dtoIn.password(),
                dtoIn.email(),
                dtoIn.firstname(), dtoIn.lastname(),
                dtoIn.street(),
                dtoIn.streetNr(),
                dtoIn.zip(),
                dtoIn.city());

        String token = securityManager.createBenutzerToken(user);
        UserDtoOut out = new UserDtoOut(user.getId(), user.getFirstname(), user.getLastname());
        return ResponseEntity.ok().header("Authorization", token).body(out);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDtoOut> login(@RequestBody LogInDtoIn dtoIn) {
        User user;
        try {
            user = dbAccess.findUserByUsername(dtoIn.username());
        } catch (NoResultException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username or Password ");
        }
        if (!PasswordTools.checkPassword(dtoIn.password(), user.getPasswordHash(), user.getPasswordSalt())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Username or Password");
        }
        String token = securityManager.createBenutzerToken(user);
        UserDtoOut out = new UserDtoOut(user.getId(), user.getFirstname(), user.getLastname());
        return ResponseEntity.ok().header("Authorization", token).body(out);
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader ("Authorization") String token){
        securityManager.removeToken(token);
        return ResponseEntity.ok().build();
    }
}