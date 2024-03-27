package az.unitech.unitech.repository;

import az.unitech.unitech.entity.Account;
import az.unitech.unitech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select case when count(u) > 0 then false else true end from Users u where u.pin = :pin")
    boolean checkPin(String pin);
    Optional<User> findUserByPin(String pin);
}
