package indi.tshoiasc.wenuc_http.utils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LambdaUtil {
    public static <T> Consumer<T> consumerWithIndex(BiConsumer<T, Integer> consumer) {
        class Obj {
            int i;
        }
        Obj obj = new Obj();
        return t -> {
            int index = obj.i++;
            consumer.accept(t, index);
        };
    }
}
