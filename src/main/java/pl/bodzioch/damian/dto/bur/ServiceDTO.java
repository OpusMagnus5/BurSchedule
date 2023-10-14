package pl.bodzioch.damian.dto.bur;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;

@Getter
@Setter
public class ServiceDTO implements Serializable {

    private long id;
    private ServiceStatusDTO status;
    private String numer;
    private String tytul;
    private String dataRozpoczeciaUslugi;
    private String dataZakonczeniaUslugi;
    private int liczbaGodzin;
    private ServiceProviderDTO dostawcaUslug;
    private AddressDTO adres;

    public Optional<AddressDTO> getAdres() {
        return Optional.ofNullable(adres);
    }
}
