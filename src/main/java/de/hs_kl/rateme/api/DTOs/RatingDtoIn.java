package de.hs_kl.rateme.api.DTOs;

public record RatingDtoIn(long poiId,int grade, String txt,Integer imageId) {
}
