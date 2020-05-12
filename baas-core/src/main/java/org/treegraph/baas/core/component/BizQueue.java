package org.treegraph.baas.core.component;

import java.util.concurrent.LinkedBlockingQueue;

public class BizQueue {

	public static LinkedBlockingQueue<String> QUEUE_REPAY_URL = new LinkedBlockingQueue<>(10000);
}
