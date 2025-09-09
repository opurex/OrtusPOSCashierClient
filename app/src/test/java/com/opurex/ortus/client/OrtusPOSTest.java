package com.opurex.ortus.client;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by nanosvir on 04 Jan 16.
 */
public class OrtusPOSTest {

    @Test
    public void testGetUniversalLog() throws Exception {
        assertEquals("Opurex:OpurexTest:testGetUniversalLog", OrtusPOS.Log.getUniversalLog());
    }

    @Test
    public void testRemovePackageNoIndex() throws Exception {
        String expected = "cannotFindIndex ";
        assertEquals(OrtusPOS.Log.removePackage(expected), expected);
    }
}