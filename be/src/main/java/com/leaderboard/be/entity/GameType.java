package com.leaderboard.be.entity;

import lombok.Getter;

@Getter
public enum GameType {

    GREEN_NECK("green-neck", "그린이 목 늘리기", false),
    GREEN_BLUE_WHITE("green-blue-white", "그린이 청기 백기", false),
    PROTECT_BABY_GREEN("protect-baby-green", "아기 그린이 지키기", false),
    QUICKNESS_GAME("quickness-game", "순발력 게임", false);

    private final String gameName;
    private final String gameNameKR;
    private final boolean lowBetter;

    GameType(String gameName, String gameNameKR, boolean lowBetter) {
        this.gameName = gameName;
        this.gameNameKR = gameNameKR;
        this.lowBetter = lowBetter;
    }
}
