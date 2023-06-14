package com.texo.challenge.models;

import java.util.Set;

public record MovieDto(int awardYear, String title, String studios, Set<String> producers, boolean winner) {
}
