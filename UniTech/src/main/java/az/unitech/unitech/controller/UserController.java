package az.unitech.unitech.controller;

import az.unitech.unitech.dto.UserDTO;
import az.unitech.unitech.dto.UserLoginDto;
import az.unitech.unitech.dto.UserRegDto;
import az.unitech.unitech.entity.User;
import az.unitech.unitech.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ObjectMapper mapper;

    //  Task 1  //
    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody UserRegDto userRegDto) {
        return userService.save(mapper.convertValue(userRegDto, User.class))
                .map(user -> ResponseEntity.ok(mapper.convertValue(user, UserDTO.class)))
                .orElseGet(() -> new ResponseEntity("Pin already exists", HttpStatus.BAD_REQUEST));
    }

    //  Task 2  //
    @PostMapping("/login")
    public ResponseEntity<UserLoginDto> login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto)
                .map(value -> ResponseEntity.ok(mapper.convertValue(value, UserLoginDto.class)))
                .orElseGet(() -> new ResponseEntity("Pin or password is incorrect", HttpStatus.NOT_FOUND));
    }
}
