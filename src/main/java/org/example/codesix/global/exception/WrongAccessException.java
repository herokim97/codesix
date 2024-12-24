package org.example.codesix.global.exception;

import lombok.Getter;

@Getter
public class WrongAccessException extends CustomException {

     public WrongAccessException(final ExceptionType exceptionType) {
         super(exceptionType);
    }

}
