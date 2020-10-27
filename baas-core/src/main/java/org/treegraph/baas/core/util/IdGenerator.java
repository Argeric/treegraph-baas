package org.treegraph.baas.core.util;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * 生产主键，生成策略为：时间戳 + 机器id + 自增id. <br/>
 */
@SuppressWarnings("serial")
public class IdGenerator implements Serializable {

	/**
	 * 机器id 全局初始化一次
	 */
	private static final int machineId = 0;

	/**
	 * 自增id生成器，可扩展为其他策略
	 */
	private static final AtomicInteger inc = new AtomicInteger(0);
	
	private static final String SEPARATOR = ".";
	
	

	/**
	 * The default constructor
	 */
	private IdGenerator() {}

	/**
	 * 获取本机的信息：网卡信息 + 进程信息 + 类加载器信息
	 * 
	 * @return 机器id
	 */
	public static int computeMachineId() {
		// 获取机器id
		final int machineBlock;
		try {
			Enumeration<NetworkInterface> ni = NetworkInterface
					.getNetworkInterfaces();
			StringBuilder str = new StringBuilder();
			while (ni.hasMoreElements()) {
				str.append(ni.nextElement().toString());
			}
			machineBlock = str.toString().hashCode() << 16;
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}

		// 获取进程id
		final int processBlock;
		{
			int process;
			try {
				process = ManagementFactory.getRuntimeMXBean().getName()
						.hashCode();
			} catch (Throwable e) {
				process = new Random().nextInt();
			}

			// 获取类加载器id
			ClassLoader loader = IdGenerator.class.getClassLoader();
			int loaderId = loader == null ? 0 : System.identityHashCode(loader);
			processBlock = (Integer.toHexString(process) +
					Integer.toHexString(loaderId))
					.hashCode() & 0xFFFF;
		}

		return machineBlock | processBlock;
	}

	/**
	 * 生成Id
	 * 
	 * @return id
	 */
	public static String generate() {
		return Integer.toHexString((int) (System.currentTimeMillis() / 1000)) +
				Integer.toHexString(machineId) +
				Integer.toHexString(inc.addAndGet(1));
	}

	/**
	 * 
	 * generateReadable:生成带有时间戳的定长id，长度:30；可读性略强，但性能较之generate()方法差了约3倍 <br/>
	 *
	 * @author tongzheng.zhang
	 * @return id
	 * @since JDK 1.6
	 */
	public static String generateReadable() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR) +
				formatNum(c.get(Calendar.MONTH) + 1) +
				formatNum(c.get(Calendar.DATE)) +
				formatNum(c.get(Calendar.HOUR_OF_DAY)) +
				formatNum(c.get(Calendar.MINUTE)) +
				formatNum(c.get(Calendar.SECOND)) + SEPARATOR +
				Integer.toHexString(machineId) +
				Integer.toHexString(inc.addAndGet(1));
	}

	/**
	 * 
	 * formatNum:格式化数字，主要为个位数加前缀零. <br/>
	 *
	 * @author tongzheng.zhang
	 * @param num num
	 * @return num
	 * @since JDK 1.6
	 */
	private static String formatNum(int num) {
		if (num < 10) {
			return "0" + num;
		}
		return String.valueOf(num);
	}
	
//	/**
//	 *
//	 * generateNumberId:生成数字id. <br/>
//	 *
//	 * @return num
//	 * @since JDK 1.6
//	 */
//	public static synchronized long generateNumberId() {
//		return 0L;
//	}
	
	public static void main(String[] args) {
		Stream.generate(IdGenerator :: generate).limit(100).forEach(System.out :: println);
	}
}
