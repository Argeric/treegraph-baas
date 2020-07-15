package org.treegraph.baas.core.util;

import com.eaio.uuid.UUIDGen;

import lombok.extern.slf4j.Slf4j;

/**
 * long型id生成器，用于生成全局的long型id
 */
@Slf4j
public class IdWorker {

    private final static byte[] MAC = UUIDGen.getMACAddress().getBytes();

    private final long workerId;

    /**
     * 时间起始标记点，作为基准，一般取系统的最近时间
     */
    private final long epoch = 1419696000000L;

    /**
     * 机器标识位数
     */
    private final long workerIdBits = 10L;

    /**
     * 机器ID最大值: 1023
     */
    private final long maxWorkerId = -1L ^ -1L << this.workerIdBits;

    /**
     * 0，并发控制
     */
    private long sequence = 0L;

    /**
     * 毫秒内自增位
     */
    private final long sequenceBits = 12L;

    private final long workerIdShift = this.sequenceBits;
    private final long timestampLeftShift = this.sequenceBits + this.workerIdBits;
    private final long sequenceMask = -1L ^ -1L << this.sequenceBits;
    private long lastTimestamp = -1L;

    private final static class IdWorkerBuilder {
        private final static IdWorker BUILDER = new IdWorker();
    }

    public static IdWorker getInstance() {
        return IdWorkerBuilder.BUILDER;
    }

    private IdWorker() {
        this(getWorkId());
    }

    private IdWorker(long workerId) {
        if (workerId > this.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                String.format("worker Id can't be greater than %d or less than 0", this.maxWorkerId));
        }
        this.workerId = workerId;
    }

    public long getId() {
        long id = nextId();
        return id;
    }

    private synchronized long nextId() {
        long timestamp = this.timeGen();

        // 如果上一个timestamp与新产生的相等，则sequence加一(0-4095循环) 对新的timestamp，sequence从0开始
        if (this.lastTimestamp == timestamp) {

            this.sequence = this.sequence + 1 & this.sequenceMask;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }

        if (timestamp < this.lastTimestamp) {
            log.error(String.format("clock moved backwards.Refusing to generate id for %d milliseconds",
                (this.lastTimestamp - timestamp)));
            throw new RuntimeException(String.format(
                "clock moved backwards.Refusing to generate id for %d milliseconds", (this.lastTimestamp - timestamp)));
        }

        this.lastTimestamp = timestamp;
        return timestamp - this.epoch << this.timestampLeftShift | this.workerId << this.workerIdShift | this.sequence;
    }

    private static long getWorkId() {

        long id = ((0x000000FF & (long)MAC[MAC.length - 1]) | (0x0000FF00 & (((long)MAC[MAC.length - 2]) << 8))) >> 6;
        return id;
    }

    /**
     * 等待下一个毫秒的到来, 保证返回的毫秒数在参数lastTimestamp之后
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    /**
     * 获得系统当前毫秒数
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }
    
    public static long generateId() {
        return IdWorker.getInstance().getId();
    }
}
