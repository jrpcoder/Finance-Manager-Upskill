package pt.upskill.projeto2.financemanager.categories.unittests;


import org.junit.Before;
import org.junit.Test;
import pt.upskill.projeto2.financemanager.categories.Category;

import static org.junit.Assert.*;

public class CategoryTest {

    private Category c1;
    private Category c2;
    private Category c3;


    @Before
    public void setUp() throws Exception {
        c1 = new Category("HOME");
        c2 = new Category("CAR");
        c3 = new Category("EXTRA");
    }


    @Test
    public void testCategory() {
        @SuppressWarnings("unused")
        Category c = new Category("HOME");
    }

    @Test
    public void testAddTag() {
        assertFalse(c1.hasDescription("PURCHASE"));
        c1.addDescription("PURCHASE");
        assertTrue(c1.hasDescription("PURCHASE"));
        assertFalse(c1.hasDescription("GASOLINA"));
        c1.addDescription("GASOLINA");
        assertTrue(c1.hasDescription("GASOLINA"));

    }

    @Test
    public void testGetName() {
        assertEquals("HOME", c1.getName());
        assertEquals("CAR", c2.getName());
        assertEquals("EXTRA", c3.getName());
    }

}
