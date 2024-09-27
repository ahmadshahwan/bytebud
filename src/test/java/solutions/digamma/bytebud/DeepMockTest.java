package solutions.digamma.bytebud;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeepMockTest {


    @Test
    void proxifyPrimitive() {
        Node node = DeepMock.proxify();
        assertNotNull(node);
        assertEquals(node.getValue(), 0);
    }

    @Test
    void proxifyRecursive() {
        NodeFactory factory = DeepMock.proxify();
        Node node = factory.getInstance();
        assertNotNull(node);
        assertNotNull(node.getNext());
        assertSame(node, node.getNext());
        assertEquals(node.getValue(), 0);
    }

    @Test
    void proxifyFinal() {
        String str = DeepMock.proxify();
        assertNotNull(str);
        assertEquals(str, "");
    }

    @Test
    void proxifyGenerics() {
        Box optStr = DeepMock.proxify();
        assertNotNull(optStr);
        assertNotNull(optStr.getValue());
        assertFalse(optStr.getValue().equals(null));
    }
}