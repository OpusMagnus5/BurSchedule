package pl.bodzioch.damian.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServiceProvider {

    BTLA_SKILLS(46039L, "BTLA SKILLS", 5272872314L),
    BOOKING_ANIMAL_SPA(124220L, "BOOKING ANIMAL SPA", 7162826739L),
    OPUS_MAGNUS(137771L, "Opus Magnus", 9542469063L);

    private final long id;
    private final String name;
    private final long nip;
}
