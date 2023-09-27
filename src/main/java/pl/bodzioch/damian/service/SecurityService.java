package pl.bodzioch.damian.service;

import pl.bodzioch.damian.exception.CipherException;

public interface SecurityService {

    String encryptMessage(String message)  throws CipherException;

    String decryptMessage(String message)  throws CipherException;
}
