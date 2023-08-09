package uz.omonako.throttler.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.omonako.throttler.configuration.RequestThrottler;

@RestController("/api")
public class RequestController {
    private final RequestThrottler requestThrottler;

    @Autowired
    public RequestController(RequestThrottler requestThrottler) {
        this.requestThrottler = requestThrottler;
    }

    @GetMapping("/status")
    public ResponseEntity<?> myEndpoint(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if (requestThrottler.canProceed(ip)) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(502));
        }
    }
}
