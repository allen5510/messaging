package com.example.demo.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HashGeneratorTests {
    // expectedOutput 來源：https://coding.tools/tw/sha256

    @Test
    public void testGenerateSHA256Hash_normal() {
        String input = "Hello World";
        String expectedOutput = "a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e";
        String actualOutput = HashGenerator.generateSHA256Hash(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGenerateSHA256Hash_emptyInput() {
        String input = "";
        String expectedOutput = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        String actualOutput = HashGenerator.generateSHA256Hash(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGenerateSHA256Hash_specialCharacters() {
        String input = "!@#$%^&*()";
        String expectedOutput = "95ce789c5c9d18490972709838ca3a9719094bca3ac16332cfec0652b0236141";
        String actualOutput = HashGenerator.generateSHA256Hash(input);
        assertEquals(expectedOutput, actualOutput);
    }
}
