package pl.bodzioch.damian.dto.bur;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ServiceScheduleDTO implements Serializable {

    private int id;
    private String temat;
    private String godzinaRozpoczecia;
    private String godzinaZakonczenia;
    private String data;

}
