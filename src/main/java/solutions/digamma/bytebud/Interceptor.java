package solutions.digamma.bytebud;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

public class Interceptor {
    @RuntimeType
    public static Object intercept(
            @Origin Method method
    ) {
        Class<?> returnType = method.getReturnType();
        if (returnType.isPrimitive()) {
            return Array.get(Array.newInstance(returnType, 1), 0);
        } else {
            return DeepMock.proxifyForClass(returnType);
        }
    }
}