package pl.bodzioch.damian.dto.bur;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ListOfServiceScheduleEntriesDTO implements Serializable {

    private int strona;
    private int elementyNaStrone;
    private int wszystkieElementy;
    private List<ServiceScheduleDTO> lista;
}
