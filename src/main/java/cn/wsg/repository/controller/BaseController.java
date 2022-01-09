package cn.wsg.repository.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Kingen
 */
class BaseController {

    static final ResponseEntity.BodyBuilder NOT_FOUND = ResponseEntity.status(HttpStatus.NOT_FOUND);
    static final ResponseEntity<String> SUCCESS = ResponseEntity.ok().body("Success");
}
