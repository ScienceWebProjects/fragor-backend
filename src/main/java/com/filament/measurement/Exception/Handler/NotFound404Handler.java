package com.filament.measurement.Exception.Handler;

import com.filament.measurement.Exception.NotFound404Exception;
import com.filament.measurement.Exception.Payload.NotFound404Payload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class NotFound404Handler {
    @ExceptionHandler(value = {NotFound404Exception.class})
    public ResponseEntity<Object> handle(NotFound404Exception exception){
       NotFound404Payload newException =  new NotFound404Payload(
                exception.getMessage()
        );
       return new ResponseEntity<>(newException,HttpStatus.NOT_FOUND);
    }
}
