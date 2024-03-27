package az.unitech.unitech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegDto {
    private String name;
    private String surname;
    private String pin;
    private String password;
}
