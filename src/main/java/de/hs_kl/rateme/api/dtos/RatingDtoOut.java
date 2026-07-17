package de.hs_kl.rateme.api.dtos;


import java.sql.Timestamp;

public record RatingDtoOut(int id,Timestamp createdAt, String userFullName,String poiName, String txt, int grade, Integer imageId) {
}
