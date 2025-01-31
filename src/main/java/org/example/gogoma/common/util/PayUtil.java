package org.example.gogoma.common.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PayUtil {

    public String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 0~9 숫자 생성
        }
        return sb.toString();
    }
}
