
package com.opurex.ortus.client.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static org.junit.Assert.*;
import org.junit.Test;

public class CashTest {

    @Test
    public void cashTest() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("cashRegister", 1);
        o.put("sequence", 1);
        o.put("csPeriod", 0.0);
        o.put("csFYear", 0.0);
        o.put("taxes", new JSONArray());
        o.put("openDate", -1);
        o.put("closeDate", -1);
        o.put("openCash", 1.0);
        o.put("closeCash", 1.0);
        o.put("expectedCash", 1.0);
        assertNotNull(new Cash(o));
    }

    // ---Pour permettre l'exécution des test----------------------                                                                                                                                                  
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(CashTest.class);
    }
}