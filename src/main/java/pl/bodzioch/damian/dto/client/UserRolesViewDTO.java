package pl.bodzioch.damian.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserRolesViewDTO implements Serializable {

    private List<String> roles;
}

