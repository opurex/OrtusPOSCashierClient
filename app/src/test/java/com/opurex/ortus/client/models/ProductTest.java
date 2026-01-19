package com.opurex.ortus.client.models;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {

    private Product product;

    @Before
    public void setUp() {
        // Initialize a product for testing
        product = new Product("test_id", "Test Product", "12345", 10.0, 12.0, 1, false, false, 0.0, false, false);
    }

    @Test
    public void testGetId() {
        assertEquals("test_id", product.getId());
    }

    @Test
    public void testGetLabel() {
        assertEquals("Test Product", product.getLabel());
    }

    @Test
    public void testGetPrice() {
        assertEquals(10.0, product.getPrice(), 0.001); // Use delta for double comparison
    }

    @Test
    public void testIsScaled_initialFalse() {
        assertFalse(product.isScaled());
    }

    @Test
    public void testSetScaled() {
        product.setScaled(true);
        assertTrue(product.isScaled());
        product.setScaled(false);
        assertFalse(product.isScaled());
    }

    @Test
    public void testEquals_sameId() {
        Product anotherProduct = new Product("test_id", "Another Product", "67890", 15.0, 18.0, 1, false, false, 0.0, false, false);
        assertTrue(product.equals(anotherProduct));
    }

    @Test
    public void testEquals_differentId() {
        Product anotherProduct = new Product("different_id", "Test Product", "12345", 10.0, 12.0, 1, false, false, 0.0, false, false);
        assertFalse(product.equals(anotherProduct));
    }

    @Test
    public void testEquals_nullIdSamePrice() {
        Product localProduct1 = new Product("Local Product", 5.0, 6.0, 1);
        Product localProduct2 = new Product("Another Local Product", 5.0, 6.0, 1);
        assertTrue(localProduct1.equals(localProduct2));
    }

    @Test
    public void testEquals_nullIdDifferentPrice() {
        Product localProduct1 = new Product("Local Product", 5.0, 6.0, 1);
        Product localProduct2 = new Product("Another Local Product", 7.0, 8.0, 1);
        assertFalse(localProduct1.equals(localProduct2));
    }

    @Test
    public void testToString() {
        assertEquals("Test Product (test_id)", product.toString());
    }
}
