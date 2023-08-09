package uz.omonako.throttler;

import org.apache.tomcat.util.digester.SystemPropertySource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.system.SystemProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uz.omonako.throttler.configuration.RequestThrottler;
import uz.omonako.throttler.controller.RequestController;
import uz.omonako.throttler.dto.ThrottleInfo;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ThrottlerApplicationTests {

  private RequestThrottler requestThrottler;

  @BeforeEach
  public void setUp() throws IOException {
    Properties properties = new Properties();
    properties.load(ThrottlerApplicationTests.class.getClassLoader().getResourceAsStream("application.properties"));
    MockitoAnnotations.openMocks(this);
    int requestLimit = Integer.parseInt(properties.getProperty("request.limit"));
    int intervalInMinutes = Integer.parseInt(properties.getProperty("request.interval.minutes"));
    requestThrottler = new RequestThrottler();

    ReflectionTestUtils.setField(requestThrottler, "requestLimit", requestLimit);
    ReflectionTestUtils.setField(requestThrottler, "intervalInMinutes", intervalInMinutes);
  }

  @RepeatedTest(20)
  void testCanProceedForDifferentIps() throws InterruptedException {
    int NUM_THREADS = 4;

    CountDownLatch latch = new CountDownLatch(NUM_THREADS);

    for (int i = 0; i < NUM_THREADS; i++) {
      Thread thread = new Thread(() -> {
        String ip = generateRandomIp();
        try {
          latch.countDown();
          latch.await(); // Wait for all threads to start simultaneously
          assertTrue(requestThrottler.canProceed(ip));
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      });
      thread.start();
    }

    latch.await();
  }

  @Test
  void testRequestThrottlingMoreRequests() {
    int numRequests = 15; // More than the request limit
    boolean result = true;
    for (int i = 0; i < numRequests; i++) {
      result &= requestThrottler.canProceed("10.10.10.10");
    }
    assertFalse(result);
  }

  @Test
  void testRequestThrottlingLessRequests() throws Exception {
    int numRequests = 9; // Less than the request limit
    boolean result = true;
    for (int i = 0; i < numRequests; i++) {
      result &= requestThrottler.canProceed("10.10.10.10");
    }
    assertTrue(result);
  }

  public static String generateRandomIp() {
    Random random = new Random();
    return random.nextInt(256) + "." +
            random.nextInt(256) + "." +
            random.nextInt(256) + "." +
            random.nextInt(256);
  }

}
