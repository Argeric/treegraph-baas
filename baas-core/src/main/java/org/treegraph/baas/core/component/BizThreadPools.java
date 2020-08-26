package org.treegraph.baas.core.component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BizThreadPools {

	public final static ExecutorService HEARTBEAT = Executors.newFixedThreadPool(5);
}
