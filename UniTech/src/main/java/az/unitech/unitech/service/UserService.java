package az.unitech.unitech.service;

import az.unitech.unitech.dto.UserLoginDto;
import az.unitech.unitech.entity.Account;
import az.unitech.unitech.entity.User;
import az.unitech.unitech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    public Optional<User> save(User user) {
        if(userRepository.checkPin(user.getPin())){
            user.setRole("USER");
            user.setPassword(encoder.encode(user.getPassword()));
            return Optional.of(userRepository.save(user));
        }else {
            return Optional.empty();
        }
    }
    public Optional<User> findUserByPin(String pin){
        return userRepository.findUserByPin(pin);
    }
    public Optional<User> findUserById(Long id){return userRepository.findById(id);}

    public Optional<User> login(UserLoginDto userLoginDto){
        return findUserByPin(userLoginDto.getPin())
                .map(user -> {
                    if (encoder.matches(userLoginDto.getPassword(), user.getPassword())){
                        return Optional.of(user);
                    } else return Optional.<User>empty();
                }).orElse(Optional.empty());
    }
}
