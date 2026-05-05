package com.leaderboard.be.service;

import com.leaderboard.be.dto.GameRankingResponse;
import com.leaderboard.be.dto.LeaderboardResponse;
import com.leaderboard.be.entity.GameType;
import com.leaderboard.be.repository.PlayCountInterface;
import com.leaderboard.be.repository.RankInterface;
import com.leaderboard.be.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LeaderboardService {

    private final ScoreRepository scoreRepository;

    public GameRankingResponse getSingleGameRankings(String gameName) {
        GameType gameType = GameType.from(gameName);

        List<RankInterface> rankInterfaces;

        if (!gameType.isLowBetter()) {
            rankInterfaces = scoreRepository.findTop5WithRankByHighScore(gameType.name());
        } else {
            rankInterfaces = scoreRepository.findTop5WithRankByLowScore(gameType.name());
        }

        List<GameRankingResponse.Ranking> rankings = rankInterfaces.stream()
                .map(entity -> new GameRankingResponse.Ranking(entity.getRank(), entity.getNickname(), entity.getScore()))
                .toList();

        return new GameRankingResponse(gameType.getGameNameKR(), rankings);
    }

    public LeaderboardResponse getOverallRanking() {
        List<PlayCountInterface> participations = scoreRepository.countParticipationPerUser();

        Map<String, Integer> scoreMap = new HashMap<>();
        Map<String, Integer> playCountMap = new HashMap<>();
        Map<String, String> nicknameMap = new HashMap<>();

        participations.forEach(p -> {
            String userId = p.getUserId();
            scoreMap.merge(userId, 3, Integer::sum);
            playCountMap.put(userId, p.getTotalPlayCount());
            nicknameMap.put(userId, p.getNickname());
        });
        addRankingPointsPerGames(scoreMap);

        return new LeaderboardResponse(getTop5Rankings(scoreMap, playCountMap, nicknameMap));
    }

    private void addRankingPointsPerGames(Map<String, Integer> scoreMap) {
        List<GameType> allGames = List.of(GameType.values());
        allGames.forEach(gameType -> addRankingPointsForGame(scoreMap, gameType));
    }

    private void addRankingPointsForGame(Map<String, Integer> scoreMap, GameType gameType) {
        if (!gameType.isLowBetter()) {
            scoreRepository.findTop5WithRankByHighScore(gameType.name()).forEach(
                    p -> scoreMap.merge(p.getUserId(), RankingPoint.getPointsByRank(p.getRank()), Integer::sum)
            );
        } else {
            scoreRepository.findTop5WithRankByLowScore(gameType.name()).forEach(
                    p -> scoreMap.merge(p.getUserId(), RankingPoint.getPointsByRank(p.getRank()), Integer::sum)
            );
        }
    }

    private List<LeaderboardResponse.LeaderboardRanking> getTop5Rankings(
            Map<String, Integer> scoreMap,
            Map<String, Integer> playCountMap,
            Map<String, String> nicknameMap
    ) {
        List<Map.Entry<String, Integer>> sorted =  scoreMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(it -> playCountMap.get(it.getKey()), Comparator.reverseOrder()))
                .limit(5)
                .toList();

        List<LeaderboardResponse.LeaderboardRanking> result = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i++) {
            Map.Entry<String, Integer> entry = sorted.get(i);
            
            result.add(new LeaderboardResponse.LeaderboardRanking(
                    i + 1,
                    nicknameMap.get(entry.getKey()),
                    entry.getValue()
            ));
        }
        
        return result;
    }
}
