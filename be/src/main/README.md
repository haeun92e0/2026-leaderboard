🎮 Game Score Management API

그리디 축제 부스에서 운영되는 미니게임들의 결과를 실시간으로 등록하고, 관리자 권한으로 점수를 수정할 수 있는 기능을 제공합니다.

📌 주요 기능
1. 게임 결과 등록 : 유저의 게임 점수를 수신하여 신규 등록하거나, 기존 기록보다 높을(또는 낮을) 경우에만 갱신합니다.
2. 유저 점수 강제 수정 : 관리자 페이지 등에서 특정 유저의 점수를 조건 없이 강제로 업데이트합니다.
3. 자동 플레이 횟수 집계 : 점수 갱신 여부와 상관없이 게임 플레이 횟수를 누적하여 동점자 순위 산정의 근거를 제공합니다.

📖 API 상세 명세
1. 게임 결과 등록
   각 미니게임 클라이언트에서 게임이 종료될 때 호출하는 API입니다.

URL: /api/result

Method: POST

Request Body (ScoreSubmitRequest)

Response (ScoreSubmitResponse)

    201 Created: 첫 기록 등록 시

    200 OK: 기존 기록 존재 시 (갱신 여부에 따라 UPDATED 또는 UNCHANGED 반환)

2. 유저 점수 강제 수정(Admin)
    운영진이 유저의 점수를 강제로 조정해야 할 때 사용하는 API입니다.

URL: /api/scores/force

Method: POST

Request Body (ScoreSubmitRequest)

Response (ScoreUpdateResponse)

    200 OK : 수정 완료 (이전 점수와 수정된 점수를 함께 반환)
