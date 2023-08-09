package uz.omonako.throttler.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import uz.omonako.throttler.dto.ThrottleInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RequestThrottler {

    @Value("${request.limit}")
    private Integer requestLimit;

    @Value("${request.interval.minutes}")
    private Integer intervalInMinutes;

    private final Map<String, ThrottleInfo> requestCountMap = new ConcurrentHashMap<>();

    // using synchronised until better approach is mind up
    public synchronized boolean canProceed(String ip) {
        long currentTime = System.currentTimeMillis();
        requestCountMap.putIfAbsent(ip, new ThrottleInfo(requestLimit, intervalInMinutes, currentTime));

        ThrottleInfo throttleInfo = requestCountMap.get(ip);
        if (throttleInfo.canProceed(currentTime)) {
            throttleInfo.incrementCount();
            return true;
        }

        return false;
    }
}
