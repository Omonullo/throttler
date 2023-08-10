package uz.omonako.throttler.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.omonako.throttler.annotations.RequestThrottle;

@RestController("/api")
public class RequestController {
    @RequestThrottle
    @GetMapping("/status")
    public ResponseEntity<Object> myEndpoint() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
