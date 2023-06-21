package com.texo.challenge.services;

import com.texo.challenge.models.AwardIntervalDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AwardIntervalsService {

    private static final String GROUP_AWARD_INTERVALS_OF_PRODUCER =
            """
                SELECT new com.texo.challenge.models.AwardIntervalDto(producer, previousWin, followingWin, (followingWin - previousWin))
                FROM (
                    SELECT p.name AS producer, m.awardYear AS previousWin,
                     (
                         SELECT MIN(mf.awardYear) 
                         FROM Movie mf 
                         JOIN MovieProducer mmp ON mmp.id.movie = mf.id 
                         WHERE mmp.id.producer = p.id 
                         AND mf.winner = true 
                         AND mf.awardYear >= m.awardYear 
                         AND mf.id != mp.id.movie 
                         GROUP BY mmp.id.producer
                     ) AS followingWin
                    FROM MovieProducer mp
                    JOIN Producer p ON p.id = mp.id.producer
                    JOIN Movie m ON m.id = mp.id.movie 
                    AND m.winner = true
                    ORDER BY m.awardYear
                ) WHERE followingWin IS NOT NULL
            """;

    @PersistenceContext
    private EntityManager entityManager;

    public List<AwardIntervalDto> getIntervalsBetweenAwards() {
        return entityManager
                .createQuery(GROUP_AWARD_INTERVALS_OF_PRODUCER, AwardIntervalDto.class)
                .getResultList();
    }
}
