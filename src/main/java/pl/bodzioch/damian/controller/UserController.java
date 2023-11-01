package pl.bodzioch.damian.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.bodzioch.damian.dto.client.CreateUserRequestDTO;
import pl.bodzioch.damian.dto.client.CreateUserResponseDTO;
import pl.bodzioch.damian.mapper.ClientMapper;
import pl.bodzioch.damian.model.UserModel;
import pl.bodzioch.damian.service.UserService;

@AllArgsConstructor
@Controller
@RequestMapping("app/user")
public class UserController {

    private ClientMapper clientMapper;
    private MessageSource messageSource;
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<CreateUserResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO request) {
        UserModel user = clientMapper.map(request);
        userService.createUser(user);
        String result = messageSource.getMessage("create.user.created", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateUserResponseDTO(result));
    }
}
