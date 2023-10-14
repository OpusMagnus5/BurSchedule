package pl.bodzioch.damian.dto.bur;

import lombok.Getter;

@Getter
public enum ServiceStatusDTO {

    OPUBLIKOWANA("published"),
    ODWOLANA("canceled"),
    ZAWIESZONA("suspended"),
    ZREALIZOWANA("completed"),
    ZABLOKOWANA("blocked"),
    NIEZREALIZOWANA("not.complete");

    private final String code;

    ServiceStatusDTO(String code) {
        this.code = code;
    }
}
