package de.hs_kl.rateme.api.DTOs;

public record RegisterDtoIn(String username, String email,
                            String password,
                            String firstname, String lastname,
                            String street,
                            String streetNr,
                            String zip,
                            String city) {
}
