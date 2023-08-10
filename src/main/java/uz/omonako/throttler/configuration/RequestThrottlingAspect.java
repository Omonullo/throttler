package uz.omonako.throttler.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uz.omonako.throttler.exceptions.LimitExceededException;

@Aspect
@Component
public class RequestThrottlingAspect {

    private final RequestThrottler requestThrottler;

    public RequestThrottlingAspect(RequestThrottler requestThrottler) {
        this.requestThrottler = requestThrottler;
    }

    @Pointcut("@annotation(uz.omonako.throttler.annotations.RequestThrottle)")
    public void throttledMethods() {
    }

    @AfterReturning(pointcut = "throttledMethods()")
    public void applyThrottling() throws LimitExceededException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String clientIp = request.getRemoteAddr();
        if (!requestThrottler.canProceed(clientIp)) {
            throw new LimitExceededException();
        }
    }
}
