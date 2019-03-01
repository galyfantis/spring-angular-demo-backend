package org.gal.fullstack.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.UNPROCESSABLE_ENTITY, reason="Cannot delete self")
public class CannotDeleteSelfException extends RuntimeException {

}
