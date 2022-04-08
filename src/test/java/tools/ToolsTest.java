package tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolsTest {

    @Test
    void formatString() {
        assertEquals("Sand  ", Tools.FormatString("Sand", 6));
        assertEquals("San",    Tools.FormatString("Sand", 3));
        assertEquals("",       Tools.FormatString("Sand", 0));
        assertEquals("      ", Tools.FormatString("", 6));
        assertEquals("",       Tools.FormatString("", 0));
    }
}