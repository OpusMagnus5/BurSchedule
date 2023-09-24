package pl.bodzioch.damian.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ServiceProvider {

    BTLA_SKILLS(46039, "BTLA SKILLS", 5272872314L),
    BOOKING_ANIMAL_SPA(124220, "BOOKING ANIMAL SPA", 7162826739L),
    OPUS_MAGNUS(137771, "Opus Magnus", 9542469063L);

    private final int id;
    private final String name;
    private final long nip;
}
