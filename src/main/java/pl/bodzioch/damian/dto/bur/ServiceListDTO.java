package pl.bodzioch.damian.dto.bur;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServiceListDTO {

    private int strona;
    private int elementyNaStrone;
    private long wszystkieElementy;
    private List<ServiceDTO> lista;
}
