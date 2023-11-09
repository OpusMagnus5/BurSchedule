package pl.bodzioch.damian.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import pl.bodzioch.damian.configuration.security.UserRoles;
import pl.bodzioch.damian.dto.client.LoginRequestDTO;
import pl.bodzioch.damian.dto.client.LoginResponseViewDTO;
import pl.bodzioch.damian.dto.client.UserRolesViewDTO;
import pl.bodzioch.damian.exception.AppException;
import pl.bodzioch.damian.mapper.ClientMapper;

import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/app/security")
@Slf4j
public class SecurityController {

    private final AuthenticationManager authenticationManager;
    private final ClientMapper clientMapper;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @PostMapping("/login")
    public ResponseEntity<LoginResponseViewDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest, HttpServletRequest request,
                                                      HttpServletResponse response) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authenticationResponse;
        try {
            authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
        } catch (Exception ex) {
            log.info("Authentication error", ex);
            throw new AppException("authentication.failed", Collections.emptyList(), HttpStatus.BAD_REQUEST);
        }

        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authenticationResponse);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);

        List<String> roles = authenticationResponse.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponseViewDTO(roles));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/roles")
    public ResponseEntity<UserRolesViewDTO> getRoles() {
        UserRolesViewDTO response = clientMapper.map(UserRoles.values());

        return ResponseEntity.ok(response);
    }
}
