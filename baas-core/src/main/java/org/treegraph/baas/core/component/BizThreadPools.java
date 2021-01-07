package org.treegraph.baas.core.component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BizThreadPools {

  public final static ExecutorService HEARTBEAT = Executors.newFixedThreadPool(20);

  public final static ExecutorService ATUO_DEPLOY = Executors.newFixedThreadPool(10);
}
