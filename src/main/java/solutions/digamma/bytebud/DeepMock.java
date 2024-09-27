package solutions.digamma.bytebud;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class DeepMock {
    private static final ProxyCache proxyCache = new ProxyCache();

    public static <T> T proxifyForClass(Class<T> klass) {
        Optional<T> optionalProxy = proxyCache.get(klass);
        if (optionalProxy.isPresent()) {
            return optionalProxy.get();
        }
        if (Modifier.isFinal(klass.getModifiers())) {
            return instantiate(klass);
        }
        try (DynamicType.Unloaded<T> build = new ByteBuddy()
                .subclass(klass)
                .method(ElementMatchers.any())
                .intercept(MethodDelegation.to(Interceptor.class))
                .make()) {
            Class<? extends T> target = build
                    .load(klass.getClassLoader())
                    .getLoaded();
            T proxy = instantiate(target);
            proxyCache.add(klass, proxy);
            return proxy;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T instantiate(Class<T> klass) {
        Constructor<T> constructor = Arrays.stream((Constructor<T>[]) klass.getConstructors())
                .min(Comparator.comparing(Constructor::getParameterCount))
                .orElseThrow();
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            params[i] = proxifyForClass(paramTypes[i]);
        }
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T proxify(T... reifier) {
        Class<T> klass = classOf(reifier);
        return proxifyForClass(klass);
    }

    private static <T> Class<T> classOf(T[] array) {
        return (Class<T>) array.getClass().getComponentType();
    }
}
