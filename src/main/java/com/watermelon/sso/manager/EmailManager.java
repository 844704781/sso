package com.watermelon.sso.manager;

import com.watermelon.sso.common.ResultCode;
import com.watermelon.sso.common.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 邮件服务实现
 */
@Service
@Slf4j
public class EmailManager {

    // 验证码过期时间（分钟）
    private static final int CODE_EXPIRE_MINUTES = 5;
    // 验证码长度
    private static final int CODE_LENGTH = 6;
    // 邮件主题
    private static final String MAIL_SUBJECT = "验证码";
    // 邮件内容模板
    private static final String MAIL_CONTENT_TEMPLATE = "您的验证码是：%s，有效期 %d 分钟，请勿泄露给他人。";
    // 验证码Redis key前缀
    private static final String REDIS_KEY_PREFIX = "email:code:";
    // 邮箱发送频率限制Redis key前缀
    private static final String REDIS_LIMIT_KEY_PREFIX = "email:limit:";
    // 邮箱发送频率限制时间（小时）
    private static final int LIMIT_EXPIRE_HOURS = 6;
    // 邮箱发送频率限制次数
    private static final int LIMIT_MAX_COUNT = 20;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${spring.mail.username}")
    private String mailFrom;

    public boolean sendVerificationCode(String email) {
        try {
            // 检查发送频率限制
            String limitKey = REDIS_LIMIT_KEY_PREFIX + email;
            String countStr = redisTemplate.opsForValue().get(limitKey);
            int count = 0;

            if (countStr != null) {
                count = Integer.parseInt(countStr);
                if (count >= LIMIT_MAX_COUNT) {
                    log.warn("Email {} has reached the sending limit of {} in {} hours", email, LIMIT_MAX_COUNT, LIMIT_EXPIRE_HOURS);
                    return false;
                }
            }

            // 生成随机验证码
            String code = generateVerificationCode(CODE_LENGTH);

            // 发送验证码邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(email);
            message.setSubject(MAIL_SUBJECT);
            message.setText(String.format(MAIL_CONTENT_TEMPLATE, code, CODE_EXPIRE_MINUTES));
            mailSender.send(message);

            // 存储验证码到Redis
            String redisKey = REDIS_KEY_PREFIX + email;
            redisTemplate.opsForValue().set(redisKey, code, CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

            // 更新发送频率计数
            count++;
            redisTemplate.opsForValue().set(limitKey, String.valueOf(count), LIMIT_EXPIRE_HOURS, TimeUnit.HOURS);

            // 使用日志记录验证码信息，便于开发调试
            log.info("Verification code for {}: {}", email, code);
            log.info("Email {} has sent {} verification codes in the last {} hours", email, count, LIMIT_EXPIRE_HOURS);

            return true;
        } catch (Exception e) {
            log.error("Failed to send verification code to email: {}", email, e);
            throw new ServiceException(ResultCode.EMAIL_SEND_LIMIT_EXCEEDED, "Email send limit exceeded");
        }
    }

    public boolean verifyEmailCode(String email, String code) {
        if (email == null || code == null) {
            return false;
        }

        // 从Redis中获取存储的验证码
        String redisKey = REDIS_KEY_PREFIX + email;
        String storedCode = redisTemplate.opsForValue().get(redisKey);

        // 验证码为空或不匹配
        if (storedCode == null || !storedCode.equals(code)) {
            return false;
        }

        // 验证成功后删除验证码
        redisTemplate.delete(redisKey);
        return true;
    }

    /**
     * 生成随机验证码
     *
     * @param length 验证码长度
     * @return 生成的验证码
     */
    private String generateVerificationCode(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }
} 