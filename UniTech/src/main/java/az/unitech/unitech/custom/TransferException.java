package az.unitech.unitech.custom;

import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public class TransferException extends RuntimeException{
    private String message;
    private HttpStatus httpStatus;

    public TransferException(String message, HttpStatus httpStatus){
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
