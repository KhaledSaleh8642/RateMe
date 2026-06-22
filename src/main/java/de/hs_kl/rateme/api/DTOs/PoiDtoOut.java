package de.hs_kl.rateme.api.DTOs;

public record PoiDtoOut(long id, Double lat, Double lon,
                        String name, String phone,
                        String amenity) {

}
