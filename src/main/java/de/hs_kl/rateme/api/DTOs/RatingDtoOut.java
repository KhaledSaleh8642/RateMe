package de.hs_kl.rateme.api.DTOs;

import org.hibernate.dialect.function.StringFunction;

import java.sql.Timestamp;

public record RatingDtoOut(int id,Timestamp created_at, String username,String poiName, String txt, int grade, Integer imageId) {
}
