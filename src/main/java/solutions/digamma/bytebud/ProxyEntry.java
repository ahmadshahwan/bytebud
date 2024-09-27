package solutions.digamma.bytebud;

record ProxyEntry<T>(
        Class<T> type,
        T object
) {}

