package com.leaderboard.be.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ScoreSubmitRequest (
    @NotBlank(message = "게임 이름은 필수입니다.")
    String gameName,

    @NotBlank(message = "유저 식별 정보는 필수입니다.")
    String userId,

    @NotBlank(message = "점수는 0 이상이어야 합니다.")
    double score

){}
