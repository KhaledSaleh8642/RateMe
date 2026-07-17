package de.hs_kl.rateme.api.dtos;

public record PoiDtoOut(long id, Double lat, Double lon,
                        String name, String phone,
                        String amenity) {

}
