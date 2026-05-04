package com.leaderboard.be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity @Getter
@Table(name = "score")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score extends BaseEntity {
    @Id
    @Column(name = "score_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "best_score")
    private double bestScore;

    @Column(name = "play_count")
    private int playCount;

    public void submitScore(double score, User user, Game game) {
        this.bestScore = score;
        this.user = user;
        this.game = game;
        this.playCount = 1;
    }

    public void updateScore(double score) {
        this.playCount++;

        boolean isLowBetter = this.game.getGameType().isLowBetter();

        if (!isLowBetter && this.bestScore < score) {
            this.bestScore = score;
        } else if (isLowBetter && this.bestScore > score) {
            this.bestScore = score;
        }
    }

    public String getNickname() {
        return this.user.getNickname();
    }

    public void forceChangeBestScore(double score) {
        this.bestScore = score;
    }

    public static Score createScore(double score, User user, Game game) {
        Score newScore = new Score();
        newScore.submitScore(score, user, game);
        return newScore;
    }
}
