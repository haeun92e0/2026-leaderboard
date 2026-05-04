package com.leaderboard.be.repository;

import com.leaderboard.be.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    // 유저 ID와 게임 ID로 기존 점수 기록을 찾는 메서드
    Optional<Score> findByUser_UserIdAndGame_GameId(String userId, Long gameId);
}
