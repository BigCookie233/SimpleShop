package io.github.bigcookie233.simpleshop.generators;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        return generateOrderNumber();
    }

    private static String generateOrderNumber() {
        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();

        // 格式化时间戳为字符串
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestampStr = dateFormat.format(new Date(timestamp));

        // 生成随机部分
        String randomPart = generateRandomPart(4);

        // 拼接订单号
        return timestampStr + randomPart;
    }

    // 生成随机部分的方法
    private static String generateRandomPart(int length) {
        StringBuilder randomPart = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randomPart.append((int) (Math.random() * 10));
        }
        return randomPart.toString();
    }
}
