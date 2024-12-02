package fr.imt.raimed2.diagnostic.model;

import fr.imt.raimed2.action.model.Action;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SyndromTest {

    @Test
    void testSyndromFields() {
        Syndrom syndrom = new Syndrom();
        syndrom.setValue("Test Value");
        syndrom.setActions(Set.of(new Action() {}));
        syndrom.setInterpretations(Set.of(new Interpretation()));

        assertEquals("Test Value", syndrom.getValue());
        assertNotNull(syndrom.getActions());
        assertNotNull(syndrom.getInterpretations());
    }
}