package uz.omonako.throttler.dto;

public class ThrottleInfo {
    private final int requestLimit;
    private final int intervalMinutes;
    private int count;
    private long lastRequestTime;

    public ThrottleInfo(int requestLimit, int intervalMinutes, long initialTime) {
        this.requestLimit = requestLimit;
        this.intervalMinutes = intervalMinutes;
        this.count = 1;
        this.lastRequestTime = initialTime;
    }

    public boolean canProceed(long currentTime) {
        int minuteInterval = intervalMinutes * 60 * 1000;
        if (currentTime - lastRequestTime > minuteInterval) {
            count = 1;
            lastRequestTime = currentTime;
            return true;
        }
        return count < requestLimit;
    }

    public void incrementCount() {
        count++;
    }
}
