package com.leaderboard.be.controller;

import com.leaderboard.be.dto.*;
import com.leaderboard.be.service.GameScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GameScoreController {

    private final GameScoreService gameScoreService;
    //service 호출해서 처리 맡김

    @PostMapping("/result") //post/api/result 요청 받음
    public ResponseEntity<ScoreSubmitResponse> registerGameResult(@Valid @RequestBody ScoreSubmitRequest request) {
        //들어오는 데이터(id,gamename, score가 ScoreSubmitRequest request에 담김)
        ScoreSubmitResponse response = gameScoreService.processGameResult(request);
        //서비스가 처리
        if ("CREATED".equals(response.status())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }//새로 생성된 점수면 201 상태코드 반환
        return ResponseEntity.ok(response); //그 외에는 200 ok
        //처음 등록한 경우> CREATED(201) , 업데이트 및 유지 > OK(200)
    }

    @PostMapping("/scores/force") //점수 강제 수정 api
    public ResponseEntity<ScoreUpdateResponse> forceUpdateUserScore(@Valid @RequestBody ScoreSubmitRequest request) {
        return ResponseEntity.ok(gameScoreService.forceUpdateScore(request));
        // OK(200)
    }
}
