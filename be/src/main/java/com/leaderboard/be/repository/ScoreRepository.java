package com.leaderboard.be.repository;

import com.leaderboard.be.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findByUser_UserId(String userId);
    Optional<Score> findByUser_UserIdAndGame_GameId(String userId, Long gameId);

    @Query("SELECT s.user.userId AS userId, s.user.nickname AS nickname, SUM(s.playCount) AS totalPlayCount " +
            "FROM Score s GROUP BY s.user.userId, s.user.nickname")
    List<PlayCountInterface> countParticipationPerUser();

    @Query(value = "WITH ranked AS ( " +
            "SELECT u.user_id, u.nickname, s.best_score as score, RANK() OVER (ORDER BY s.best_score DESC) AS `rank` " +
            "FROM score s " +
            "JOIN users u ON s.user_id = u.user_id " +
            "JOIN game g ON s.game_id = g.game_id " +
            "WHERE g.name = :gameType " +
            " ) " +
            "SELECT * FROM ranked WHERE `rank` <= 5 ORDER BY `rank` ASC ", nativeQuery = true)
    List<RankInterface> findTop5WithRankByHighScore(@Param("gameType") String gameType);

    @Query(value = "WITH ranked AS ( " +
            "SELECT u.user_id, u.nickname, s.best_score as score, RANK() OVER (ORDER BY s.best_score ASC) AS `rank` " +
            "FROM score s " +
            "JOIN users u ON s.user_id = u.user_id " +
            "JOIN game g ON s.game_id = g.game_id " +
            "WHERE g.name = :gameType " +
            " ) " +
            "SELECT * FROM ranked WHERE `rank` <= 5 ORDER BY `rank` ASC ", nativeQuery = true)
    List<RankInterface> findTop5WithRankByLowScore(@Param("gameType") String gameType);
}
