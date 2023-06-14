package com.texo.challenge.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"producer", "interval", "previousWin", "followingWin"})
public record AwardIntervalDto(@JsonProperty String producer, @JsonProperty int previousWin,
                               @JsonProperty int followingWin, @JsonProperty("interval") int intervalYears) {
}
