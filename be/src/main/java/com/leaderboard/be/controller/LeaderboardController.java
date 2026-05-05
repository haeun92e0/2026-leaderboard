package com.leaderboard.be.controller;

import com.leaderboard.be.dto.GameRankingResponse;
import com.leaderboard.be.dto.LeaderboardResponse;
import com.leaderboard.be.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leader-board")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/{gameName}")
    public GameRankingResponse getSingleGameRankings(@PathVariable String gameName) {
        return leaderboardService.getSingleGameRankings(gameName);
    }

    @GetMapping("/overall")
    public LeaderboardResponse getOverallRanking() {
        return leaderboardService.getOverallRanking();
    }
}
