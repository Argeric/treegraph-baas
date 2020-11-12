package org.treegraph.baas.core.component;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class BizTimer {

  private static final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();

  private static final ThreadFactory customThreadfactory = threadFactoryBuilder
      .setNameFormat("DelayTimer-Thread")
      .setDaemon(false)
      .setPriority(Thread.MAX_PRIORITY)
      .build();

  public static Timer DELAY_TIMER = new HashedWheelTimer(customThreadfactory, 1, TimeUnit.SECONDS,
      60);

  public static void main(String[] argv) throws InterruptedException {
    final Timer timer = BizTimer.DELAY_TIMER;
    timer.newTimeout(timeout -> System.out.println("timeout 5"), 5, TimeUnit.SECONDS);
    timer.newTimeout(timeout -> System.out.println("timeout 10"), 10, TimeUnit.SECONDS);
    TimeUnit.SECONDS.sleep(100);
  }
}
