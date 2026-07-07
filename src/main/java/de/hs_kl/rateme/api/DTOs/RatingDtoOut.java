package de.hs_kl.rateme.api.DTOs;


import java.sql.Timestamp;

public record RatingDtoOut(int id,Timestamp createdAt, String username,String poiName, String txt, int grade, Integer imageId) {
}
