package com.leaderboard.be.dto;

import lombok.Builder;

@Builder
public record ScoreSubmitRequest (

    String gameName,
    String userId,
    double score

){}
