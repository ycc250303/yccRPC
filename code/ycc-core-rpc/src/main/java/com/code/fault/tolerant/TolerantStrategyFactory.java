package com.code.fault.tolerant;

import com.code.spi.SpiLoader;

/**
 * 容错策略工厂
 */
public class TolerantStrategyFactory {

    static {
        SpiLoader.load(TolerantStrategy.class);
    }

    /**
     * 默认容错策略（快速失败）
     */
    private static final TolerantStrategy DEFAULT_TOLERANT_STRATEGY = new FastFailTolerantStrategy();

    public static TolerantStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerantStrategy.class, key);
    }
}
