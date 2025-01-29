package org.example.gogoma.external.openai;

import lombok.Getter;

@Getter
public enum Prompt {
    DEFAULT("아래 마라톤 정보는 Google Vision API OCR과 Google Document AI를 통해 읽어들인 마라톤 포스터 정보야 두 정보에 중복되는 내용이 있지만 꼼꼼히 읽고 상호보완적으로 정의한 필드에 값을 채워나가자 구체적으로 Marathon 엔티티와 MarathonType 엔티티의 필드에 매핑하여 JSON 형태로 작성해줘. Marathon 엔티티 필드는 다음과 같아. title은 대회명, registrationStartDateTime은 접수 시작일시, registrationEndDateTime은 접수 종료일시, raceStartTime은 대회 시작일시, accountBank는 입금은행, accountNumber는 입금계좌번호, accountName은 계좌명, location은 대회 장소, hostList는 주최(여러 값일 경우 배열로), organizerList는 주관(여러 값일 경우 배열로), sponsorList는 후원(여러 값일 경우 배열로), qualifications는 참가 자격이야 그리고 MarathonType 엔티티 필드는 다음과 같아. courseType은 모든 종목(예: 5km, 10km, half, full)과, price는 참가비(종목별 가격), etc는 비회원, 회원, 비회원(매니아), 회원(매니아) 같은 정보를 담는 필드야.\n" +
            "\n" +
            "JSON 형식은 다음과 같은 형태로 작성해줘. 예를 들어, { \"marathonInfo\": { \"title\": \"대회명\", \"registrationStartDateTime\": \"2025-01-01T09:00:00\", \"registrationEndDateTime\": \"2025-01-31T17:00:00\", \"raceStartTime\": \"2025-02-15T08:00:00\", \"accountBank\": \"은행명\", \"accountNumber\": \"계좌번호\", \"accountName\": \"계좌명\", \"location\": \"대회 장소\", \"hostList\": [\"주최1\", \"주최2\"], \"organizerList\": [\"주관1\"], \"sponsorList\": [\"후원1\", \"후원2\"], \"qualifications\": \"참가 자격\", \"marathonTypeDtoList\": [ { \"courseType\": \"5km\", \"price\": \"30000\", \"etc\": \"비회원\" }, { \"courseType\": \"5km\", \"price\": \"25000\", \"etc\": \"회원\" }, { \"courseType\": \"5km\", \"price\": \"20000\", \"etc\": \"비회원(매니아)\" }, { \"courseType\": \"5km\", \"price\": \"15000\", \"etc\": \"회원(매니아)\" } ] } 형태로 작성해. \n" +
            "\n" +
            "아래에 제공된 마라톤 정보를 참고하고, 명시되지 않은 값은 빈 문자열(\"\") 로 설정해 줘. 이 때 각 종목별 비용은 절대 0이 될 수 없으니 정보를 잘 읽어보고 다시 매핑해줘. 그리고 마라톤이기 때문에 종목은 반드시 5km이상이야.\n" +
            "\n" +
            "마라톤 정보: <여기에 텍스트 형식의 마라톤 정보를 제공>");

    private final String prompt;

    Prompt(String prompt) {
        this.prompt = prompt;
    }
}
