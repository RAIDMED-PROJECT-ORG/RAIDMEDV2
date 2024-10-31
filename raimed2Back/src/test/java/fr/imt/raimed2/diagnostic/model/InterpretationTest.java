package fr.imt.raimed2.diagnostic.model;

import fr.imt.raimed2.action.model.Action;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InterpretationTest {

    @Test
    void testInterpretationFields() {
        Action action = new Action() {};
        Interpretation interpretation = new Interpretation();
        interpretation.setValue("Test Value");
        interpretation.setAction(action);

        assertEquals("Test Value", interpretation.getValue());
        assertEquals(action, interpretation.getAction());
    }
}