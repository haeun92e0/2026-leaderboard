package com.leaderboard.be.repository;

import com.leaderboard.be.entity.Game;
import com.leaderboard.be.entity.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    // 게임 타입 내부의 영문 이름(gameName)으로 게임을 찾는 메서드
    Optional<Game> findByGameType_GameName(String gameName);
}
