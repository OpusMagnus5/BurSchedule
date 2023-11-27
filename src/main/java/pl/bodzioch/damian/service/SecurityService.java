package pl.bodzioch.damian.service;

public interface SecurityService {

    String encryptMessage(String message);

    String decryptMessage(String message);
}
