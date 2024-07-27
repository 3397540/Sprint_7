package ru.yandex.practicum;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Courier {

    private String login;
    private String password;
    private String firstName;
}
