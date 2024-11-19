package edu.java.springsecuritytask.bruteforce;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
@Service
public class BruteForceProtectionService {
    private static final int MAX_ATTEMPT = 3;
    private static final long LOCK_TIME = TimeUnit.MINUTES.toMillis(5);

    private final Map<String, Integer> attemptsCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lockCache = new ConcurrentHashMap<>();

    public void loginSuccess(String key) {
        attemptsCache.remove(key); // Clear failed attempts on successful login
        lockCache.remove(key); // Unlock user on successful login
    }

    public void failLogin(String key) {
        int attempts = attemptsCache.getOrDefault(key, 0);
        attempts++;
        attemptsCache.put(key, attempts);
        if (attempts >= MAX_ATTEMPT) {
            lockCache.put(key, System.currentTimeMillis()); // Lock user if max attempts exceeded
        }
    }

    public boolean isBlocked(String key) {
        if (!lockCache.containsKey(key)) {
            return false;
        }

        long lockTime = lockCache.get(key);
        if (System.currentTimeMillis() - lockTime > LOCK_TIME) {
            lockCache.remove(key); // Remove lock if lock time has expired
            return false;
        }

        return true; // User is still locked
    }
}
