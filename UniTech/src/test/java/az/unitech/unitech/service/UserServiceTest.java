package az.unitech.unitech.service;

import az.unitech.unitech.dto.UserLoginDto;
import az.unitech.unitech.entity.User;
import az.unitech.unitech.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder encoder;

    private final User user = User.builder()
            .id(1L)
            .pin("test-pin")
            .name("test-name")
            .surname("test-surname")
            .password("test-password")
            .build();
    private final UserLoginDto userLogin = UserLoginDto.builder()
            .pin("test-pin")
            .password("test-password")
            .build();

    @Test
    void saveWhenCheckPinTrueThenUser() {

        Mockito.when(encoder.encode(user.getPassword())).thenReturn("password-encoded");
        Mockito.when(userRepository.checkPin(user.getPin())).thenReturn(true);
        Mockito.when(userRepository.save(user)).thenReturn(user);

        Optional<User> result = userService.save(user);

        assertEquals(result,Optional.of(user));
    }

    @Test
    void saveWhenCheckPinFalseThenEmpty(){

        Mockito.when(userRepository.checkPin(user.getPin())).thenReturn(false);

        Optional<User> result = userService.save(user);

        assertEquals(result,Optional.empty());
    }

    @Test
    void loginWhenPinAndPasswordTrueThenUser() {

        Mockito.when(userService.findUserByPin(userLogin.getPin())).thenReturn(Optional.of(user));
        Mockito.when(encoder.matches(userLogin.getPassword(), user.getPassword())).thenReturn(true);

        Optional<User> optionalUser = userService.login(userLogin);

        assertEquals(optionalUser,Optional.of(user));
    }

    @Test
    public void loginWhenPinFalseThenEmpty(){

        Mockito.when(userService.findUserByPin(userLogin.getPin())).thenReturn(Optional.empty());

        Optional<User> optionalUser = userService.login(userLogin);

        assertEquals(optionalUser,Optional.empty());
    }

    @Test
    public void loginWhenPasswordFalseThenEmpty(){
        Mockito.when(userService.findUserByPin(userLogin.getPin())).thenReturn(Optional.of(user));
        Mockito.when(encoder.matches(userLogin.getPassword(), user.getPassword())).thenReturn(false);

        Optional<User> optionalUser = userService.login(userLogin);

        assertEquals(optionalUser,Optional.empty());
    }
}