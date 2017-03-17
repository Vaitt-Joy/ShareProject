package com.vz.share.utils;

import com.vz.share.config.ConfigInfo;

import java.util.Date;

/**
 * 主键工具类，适用于分布式环境中仍能保持唯一
 */
public class IdWorkerUtil {

    private static IdWorker idWorker = new IdWorker(ConfigInfo.WORKER_ID, ConfigInfo.DATACENTER_ID);

    /**
     * 生成关键主键ID
     * 该主键生成策略采用Twitter的分布式自增ID算法Snowflake实现，可靠性优于UUID，适用于更严格的场景如订单编号生成
     *
     * @return
     */
    public static long generateId() {
        return idWorker.nextId();
    }

    /**
     * 生成关键主键ID
     * 该主键生成策略采用Twitter的分布式自增ID算法Snowflake实现，可靠性优于UUID，适用于更严格的场景如订单编号生成
     *
     * @return
     */
    public static String generateIdString() {
        long id = generateId();
        return String.valueOf(id);
    }

    /**
     * 生成关键主键ID，以日期开头
     * 该主键生成策略采用Twitter的分布式自增ID算法Snowflake实现，可靠性优于UUID，适用于更严格的场景如订单编号生成
     *
     * @return
     */
    public static String generateIdStartWithDate() {
        String dateStr = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        return dateStr + generateId();
    }
}

/**
 * 主键生成器
 */
class IdWorker {
    private long workerId;
    private long datacenterId;
    private long sequence = 0L;

    private long twepoch = 1288834974657L;

    private long workerIdBits = 5L;
    private long datacenterIdBits = 5L;
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private long sequenceBits = 12L;

    private long workerIdShift = sequenceBits;
    private long datacenterIdShift = sequenceBits + workerIdBits;
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    private long lastTimestamp = -1L;

    public IdWorker(long workerId, long datacenterId) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
