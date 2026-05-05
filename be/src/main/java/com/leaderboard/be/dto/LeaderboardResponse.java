package com.leaderboard.be.dto;

import java.util.List;

public record LeaderboardResponse(List<LeaderboardRanking> rankings) {
    public record LeaderboardRanking(int rank, String nickname, int score) {}
}
