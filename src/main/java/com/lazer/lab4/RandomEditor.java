package com.lazer.lab4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomEditor {
    private final Random random = new Random();

    public String getRandomElementId(RGA rga) {
        List<String> ids = new ArrayList<>(rga.elements.keySet());
        ids.remove("ROOT");
        if (ids.isEmpty()) return "ROOT";
        return ids.get(random.nextInt(ids.size()));
    }

    // Случайный символ a-z
    public char randomChar() {
        return (char) ('a' + random.nextInt(26));
    }

    // Случайное слово из 3-6 букв
    public String randomWord() {
        int length = 3 + random.nextInt(4);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(randomChar());
        }
        return sb.toString();
    }
}