package de.hs_kl.rateme.api.DTOs;

public record RegisterDtoIn(String username, String password,
                            String email,
                            String firstname, String lastname,
                            String street,
                            String streetNr,
                            String zip,
                            String city,byte[] password_hash) {
}
