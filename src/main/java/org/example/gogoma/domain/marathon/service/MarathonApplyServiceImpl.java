package org.example.gogoma.domain.marathon.service;

import lombok.extern.slf4j.Slf4j;
import org.example.gogoma.domain.marathon.dto.UserApplyMarathonDto;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class MarathonApplyServiceImpl implements MarathonApplyService {
    /** 데이터 예시
     *
     * @param userApplyMarathonDto
     *          UserApplyMarathonDto userApplyMarathonDto = new UserApplyMarathonDto(
     *                 "10km",        // 종목: km
     *                 "100",              // 기념품: 옷사이즈
     *                 "김지수",            // 성명
     *                 "삼선교로 3길 4",     // 도로명 주소
     *                 "301호",             // 상세주소
     *                 "2000", "3", "5",  // 생년월일
     *                 "1",                // 성별: 남자
     *                 "01066834380",      // 전화번호
     *                 "email123@naver.com",  // 이메일
     *                 "김지수",            // 입금자명
     *                 "123456"               // 비밀번호(4~16자리)
     *         );
     * @param marathonApplyUrl
     *      String marathonApplyUrl = "http://www.najurun.kr/run/1210.asp?T=3&wgu=1&fAgree="; //나주 영산강
     *
     * @param formNumber
     *    int formNumber1 = 1;
     */

    @Override
    public void applyMarathon(UserApplyMarathonDto userApplyMarathonDto, String marathonApplyUrl, int formNumber) {


        // ChromeOptions 설정
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless"); // 브라우저 창 없이 실행
//        options.addArguments("--disable-gpu"); // GPU 사용 비활성화 (리소스 절약)
//        options.addArguments("--no-sandbox"); // 샌드박스 모드 비활성화 (속도 개선)
//        options.addArguments("--blink-settings=imagesEnabled=false"); // 이미지 로드 비활성화
        options.addArguments("--disable-extensions"); // 확장 프로그램 비활성화
        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(marathonApplyUrl);

            // 페이지 로딩 대기
            Thread.sleep(100);
            if(formNumber == 1) { //트레일런 폼 작동
                TrailrunMarathonForm(driver, userApplyMarathonDto);
            } else if(formNumber == 2) { //전마협 폼 작동
                AllGroupMarathonForm(driver, userApplyMarathonDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            driver.quit();
        }
    }

    private static void TrailrunMarathonForm(WebDriver driver, UserApplyMarathonDto userApplyMarathonDto) throws InterruptedException {
        Map<String, String> eventMapping = new HashMap<>();
        eventMapping.put("5km-학생", "000");
        eventMapping.put("5km-일반", "001");
        eventMapping.put("5km", "001");
        eventMapping.put("10km", "002");
        eventMapping.put("하프", "003");
        eventMapping.put("풀", "004");

        // 마라톤 종류 선택
        String selectedEvent = userApplyMarathonDto.getEvent();
        try {
            List<WebElement> eventOptions = driver.findElements(By.cssSelector("input[name='wjong']"));
            WebElement eventOption = null;

            for (WebElement option : eventOptions) {
                String value = option.getAttribute("value");

                if (value != null && value.contains(selectedEvent)) {
                    eventOption = option;
                    break;
                } else if (value != null && value.contains("00")) {
                    String mappedValue = eventMapping.get(selectedEvent);
                    if (value.equals(mappedValue)) {
                        eventOption = option;
                        break;
                    }
                }
            }

            eventOption.click();
        } catch (Exception e) {
            log.info("log error: 라디오 버튼 선택 중 오류 발생");
        }

        try {
            String selectedSouvenir = userApplyMarathonDto.getSouvenir();
            WebElement souvenirOption;

            try {
                souvenirOption = driver.findElement(By.cssSelector("input[name='wts'][value='" + selectedSouvenir + "']"));
            } catch (Exception e) {
                log.info("해당 기념품 옵션 없음, 첫 번째 옵션 선택");
                // 첫 번째 기념품 요소 선택
                souvenirOption = driver.findElement(By.cssSelector("input[name='wts']"));
            }

            souvenirOption.click();
        } catch (Exception e) {
            log.info("기념품 선택 중 오류 발생");
        }

        // 성명 입력
        WebElement nameInput = driver.findElement(By.name("wname"));
        nameInput.sendKeys(userApplyMarathonDto.getName());

        // 생년월일 입력
        WebElement birthYearSelect = driver.findElement(By.name("syy"));
        birthYearSelect.sendKeys(userApplyMarathonDto.getBirthYear());
        WebElement birthMonthSelect = driver.findElement(By.name("smm"));
        birthMonthSelect.sendKeys(userApplyMarathonDto.getBirthMonth());
        WebElement birthDaySelect = driver.findElement(By.name("sdd"));
        birthDaySelect.sendKeys(userApplyMarathonDto.getBirthDay());

        // 성별 선택
        WebElement genderOption = driver.findElement(By.cssSelector("input[name='wsex'][value='" + userApplyMarathonDto.getGender() + "']"));
        genderOption.click();

        // 전화번호 입력
        WebElement phoneInput = driver.findElement(By.name("wtel"));
        phoneInput.sendKeys(userApplyMarathonDto.getPhoneNumber());
        WebElement phoneInput2 = driver.findElement(By.name("whp"));
        phoneInput2.sendKeys(userApplyMarathonDto.getPhoneNumber());

        // 주소 입력
        WebElement addressButton = driver.findElement(By.cssSelector("input[type='button'][value='우편번호 찾기']"));

        addressButton.click();
        Thread.sleep(300);

        // 팝업 창으로 전환
        String mainWindow = driver.getWindowHandle();
        Set<String> windows = driver.getWindowHandles();

        for (String window : windows) {
            if (!window.equals(mainWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }

        // iframe으로 전환
        WebElement iframeElement = driver.findElement(By.id("__daum__viewerFrame_1"));
        driver.switchTo().frame(iframeElement);

        // iframe 내부 요소에 접근
        WebElement inputField = driver.findElement(By.id("region_name"));
        inputField.sendKeys(userApplyMarathonDto.getRoadAddress(), Keys.ENTER);
        Thread.sleep(300); // 검색 결과 로딩 대기

        // 검색 결과로 나온 첫 번째 지번 주소 클릭
        WebElement firstAddressButton = driver.findElement(By.cssSelector(".list_post_item:nth-of-type(1) .rel_jibun .link_post"));
        firstAddressButton.click();

        driver.switchTo().window(mainWindow);
        // 상세주소 입력
        WebElement detailAddressInput = driver.findElement(By.name("address2"));
        detailAddressInput.sendKeys(userApplyMarathonDto.getAddressDetail());

        // 이메일 입력
        WebElement emailInput = driver.findElement(By.name("wmail"));
        emailInput.sendKeys(userApplyMarathonDto.getEmail());

        // 입금자명 입력
        WebElement depositorInput = driver.findElement(By.name("wibname"));
        depositorInput.sendKeys(userApplyMarathonDto.getDepositor());

        // 비밀번호 입력
        WebElement passwordInput = driver.findElement(By.name("wpass"));
        passwordInput.sendKeys(userApplyMarathonDto.getPassword());

        WebElement saveButton = driver.findElement(By.cssSelector("input[type='image'][name='write']"));
        log.info("form submit complete");
//        saveButton.click();
    }

    private static void AllGroupMarathonForm(WebDriver driver, UserApplyMarathonDto userApplyMarathonDto) throws InterruptedException {
        try {
            // 1. 이름 입력
            WebElement nameField = driver.findElement(By.name("f_name"));
            nameField.sendKeys(userApplyMarathonDto.getName());

            // 2. 생년월일 입력
            String year = userApplyMarathonDto.getBirthYear();
            String month = String.format("%02d", Integer.parseInt(userApplyMarathonDto.getBirthMonth())); // 두 자리 변환
            String day = String.format("%02d", Integer.parseInt(userApplyMarathonDto.getBirthDay()));   // 두 자리 변환

            String juminFront = year.substring(2) + month + day;

            WebElement birthInput = driver.findElement(By.name("f_regino1"));
            birthInput.sendKeys(juminFront);

            // 3. 성별 선택
            String genderValue = userApplyMarathonDto.getGender().equals("1") ? "1" : "0";
            WebElement genderRadio = driver.findElement(By.cssSelector("input[name='f_sex'][value='" + genderValue + "']"));
            genderRadio.click();

            // 4. 주소 입력
            WebElement addressButton = driver.findElement(By.cssSelector("input[type='text'][id='r_zip01Id']"));
            addressButton.click();
            Thread.sleep(300); // 팝업 로딩 대기

            // 5. 팝업 창 전환
            String mainWindow = driver.getWindowHandle();
            Set<String> windows = driver.getWindowHandles();
            for (String window : windows) {
                if (!window.equals(mainWindow)) {
                    driver.switchTo().window(window);
                    break;
                }
            }

            // 6. iframe 전환 (다음 우편번호 서비스)
            WebElement iframeElement = driver.findElement(By.id("__daum__viewerFrame_1"));
            driver.switchTo().frame(iframeElement);

            WebElement inputField = driver.findElement(By.id("region_name"));
            inputField.sendKeys(userApplyMarathonDto.getRoadAddress(), Keys.ENTER);
            Thread.sleep(300); // 검색 결과 로딩 대기

            WebElement firstAddressButton = driver.findElement(By.cssSelector(".list_post_item:nth-of-type(1) .rel_jibun .link_post"));
            firstAddressButton.click();

            driver.switchTo().window(mainWindow);

            // 5. 상세주소 입력
            WebElement addressDetailField = driver.findElement(By.name("address2"));
            addressDetailField.sendKeys(userApplyMarathonDto.getAddressDetail());

            // 6. 전화번호 입력
            WebElement phoneField1 = driver.findElement(By.name("tel2"));
            WebElement phoneField2 = driver.findElement(By.name("tel3"));
            WebElement phoneField3 = driver.findElement(By.name("hp2"));
            WebElement phoneField4 = driver.findElement(By.name("hp3"));
            phoneField1.sendKeys(userApplyMarathonDto.getPhoneNumber().substring(3, 7));
            phoneField2.sendKeys(userApplyMarathonDto.getPhoneNumber().substring(7));
            phoneField3.sendKeys(userApplyMarathonDto.getPhoneNumber().substring(3, 7));
            phoneField4.sendKeys(userApplyMarathonDto.getPhoneNumber().substring(7));

            // 7. 이메일 입력
            String[] emailParts = userApplyMarathonDto.getEmail().split("@");
            WebElement emailField1 = driver.findElement(By.name("email1"));
            WebElement emailField2 = driver.findElement(By.name("email2"));
            emailField1.sendKeys(emailParts[0]);
            emailField2.sendKeys(emailParts[1]);

            // 8. 참가 종목 선택
            WebElement eventSelect = driver.findElement(By.name("f_meeting_detail_no"));
            List<WebElement> eventOptions = eventSelect.findElements(By.tagName("option"));
            Map<String, String> eventMapping = new HashMap<>();
            eventMapping.put("5km", "5Km");
            eventMapping.put("10km", "10Km");
            eventMapping.put("하프", "Half");
            eventMapping.put("풀", "Full");

            String mappedValue = eventMapping.getOrDefault(userApplyMarathonDto.getEvent(), userApplyMarathonDto.getEvent());
            for (WebElement eventOption : eventOptions) {
                if (eventOption.getText().trim().equals(mappedValue)) {
                    eventOption.click();
                    break;
                }
            }

            // 9. 기념품 선택, <option> 값 중, userApplyMarathonDto.getSouvenir() 값이 포함된 요소 찾기
            WebElement souvenirSelect = driver.findElement(By.name("f_souvenir"));

            List<WebElement> souvenirOptions = souvenirSelect.findElements(By.tagName("option"));

            for (WebElement souvenirOption : souvenirOptions) {
                if (souvenirOption.getText().contains(userApplyMarathonDto.getSouvenir())) {
                    souvenirOption.click();
                    break;
                }
            }

            // 10. 입금자명 입력
            WebElement payerField = driver.findElement(By.name("f_ordername"));
            payerField.sendKeys(userApplyMarathonDto.getName());

            // 11. 비밀번호 입력
            List<WebElement> passwordFields = driver.findElements(By.cssSelector("input[type='password']"));

            for (WebElement passwordField : passwordFields) {
                passwordField.sendKeys(userApplyMarathonDto.getPassword());
            }

            // 12. 참가 신청 버튼 클릭
            WebElement submitButton = driver.findElement(By.xpath("//a[contains(@onclick, 'check_form')]"));
            log.info("form submit complete");
//            submitButton.click();

        } catch (Exception e) {
            log.error("폼 자동 입력 중 오류 발생");
        }
    }
}
