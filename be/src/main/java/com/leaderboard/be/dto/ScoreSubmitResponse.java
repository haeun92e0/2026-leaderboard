package com.leaderboard.be.dto;
import lombok.Builder;

@Builder
public record ScoreSubmitResponse ( //응답 dto

    String status, //CREATED, UPDATED, UNCHANGED //상태가 어떻게 변경되었는지
    String message, //"최고 점수가 갱신되었습니다" 등
    String gameNameKR // 게임 이름
){}
