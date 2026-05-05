package com.leaderboard.be.dto;

import java.util.List;

public record GameRankingResponse(String gameName, List<Ranking> rankings) {
    public record Ranking(int rank, String nickname, double score) {}
}
