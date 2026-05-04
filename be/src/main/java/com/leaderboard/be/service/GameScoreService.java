package com.leaderboard.be.service;

import com.leaderboard.be.dto.ScoreSubmitRequest;
import com.leaderboard.be.dto.ScoreSubmitResponse;
import com.leaderboard.be.dto.ScoreUpdateResponse;
import com.leaderboard.be.entity.Game;
import com.leaderboard.be.entity.Score;
import com.leaderboard.be.entity.User;
import com.leaderboard.be.repository.GameRepository;
import com.leaderboard.be.repository.ScoreRepository;
import com.leaderboard.be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor //final 붙은 repository를 자동으로 생성자 주입해줌
@Transactional //DB 작업이 하나의 묶음으로 처리( 중간에 에러나면 DB 반영 취소 )
public class GameScoreService {

    private final ScoreRepository scoreRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    public ScoreSubmitResponse processGameResult(ScoreSubmitRequest request) {
        // 1. 유저와 게임 존재 여부 확인
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 ID입니다: " + request.userId()));

        // 게임 찾기
        Game game = gameRepository.findByGameType_GameName(request.gameName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게임 이름입니다: " + request.gameName()));

        // 2. 기존 점수 기록 조회
        Optional<Score> optionalScore = scoreRepository.findByUser_UserIdAndGame_GameId(user.getUserId(), game.getGameId());

        if (optionalScore.isEmpty()) {
            // [CASE 1] 신규 등록 (CREATED)
            Score newScore = Score.createScore(request.score(), user, game);
            scoreRepository.save(newScore);

            return ScoreSubmitResponse.builder()
                    .status("CREATED")
                    .message("점수가 성공적으로 등록되었습니다.")
                    .gameNameKR(game.getGameType().getGameNameKR())
                    .build();
        }

        // [CASE 2] 기존 기록 존재 (비교 후 업데이트)
        Score existingScore = optionalScore.get();
        double previousBest = existingScore.getBestScore();

        // 엔티티 내부에 정의된 업데이트 로직 실행 (점수 비교 및 play_count 증가 포함)
        existingScore.updateScore(request.score());

        if (previousBest != existingScore.getBestScore()) {//업데이트 전 최고점과 업데이트 후 최고점 비교
            return ScoreSubmitResponse.builder()
                    .status("UPDATED")
                    .message("새로운 최고 기록으로 갱신되었습니다.")
                    .gameNameKR(game.getGameType().getGameNameKR())
                    .build();
        } else {
            return ScoreSubmitResponse.builder()
                    .status("UNCHANGED")
                    .message("기존 기록보다 좋지 않아 점수가 유지되었습니다.")
                    .gameNameKR(game.getGameType().getGameNameKR())
                    .build();
        }
    }

    //점수 강제 수정
    public ScoreUpdateResponse forceUpdateScore(ScoreSubmitRequest request) {
        Game game = gameRepository.findByGameType_GameName(request.gameName())
                .orElseThrow(() -> new IllegalArgumentException("게임을 찾을 수 없습니다."));

        Score score = scoreRepository.findByUser_UserIdAndGame_GameId(request.userId(), game.getGameId())
                .orElseThrow(() -> new IllegalStateException("해당 유저의 게임 기록이 존재하지 않아 수정할 수 없습니다."));

        double previousScore = score.getBestScore();

        // 엔티티에 직접 접근하여 점수 강제 변경
        score.forceChangeBestScore(request.score());

        return ScoreUpdateResponse.builder()
                .status("UPDATED")
                .message("관리자 권한으로 점수가 강제 수정되었습니다.")
                .gameNameKR(game.getGameType().getGameNameKR())
                .previousScore(previousScore)
                .updatedScore(request.score())
                .build();
    }
}
