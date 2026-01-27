package com.jichan.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptorTest {

    private Encryptor encryptor;

    @BeforeEach
    void setUp() {
        encryptor = new Encryptor();
        encryptor.setSecret("test-secret-key");
    }

    @Test
    void encryptAndDecrypt() {
        String originalString = "Hello, World!";
        String encryptedString = encryptor.encrypt(originalString);
        assertNotNull(encryptedString);
        assertNotEquals(originalString, encryptedString);

        String decryptedString = encryptor.decrypt(encryptedString);
        assertEquals(originalString, decryptedString);
    }
}
