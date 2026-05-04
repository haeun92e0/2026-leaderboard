package com.leaderboard.be.dto;

import lombok.Builder;

@Builder
public record ScoreUpdateResponse (

    String status,
    String message,
    String gameNameKR,
    double previousScore, //이전 점수
    double updatedScore //업데이트된 점수
){}
