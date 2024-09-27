package solutions.digamma.bytebud;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class ProxyCache {

    List<ProxyEntry<?>> list = Collections.synchronizedList(new ArrayList<>());

    public <T> void add(Class<T> type, T object) {
        if (this.get(type).isEmpty()) {
            list.add(new ProxyEntry<T>(type, object));
        } else {
            throw new IllegalArgumentException("Proxy already exists");
        }
    }

    public <T> Optional<T> get(Class<T> type) {
        return list
                .stream()
                .filter(e -> e.type().equals(type))
                .findAny()
                .map(e -> (T) e.object());
    }
}
