package fr.imt.raimed2.action.model;

import fr.imt.raimed2.virtualPatient.model.VirtualPatient;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {

    @Test
    void testActionFields() {
        UUID id = UUID.randomUUID();
        String primaryElement = "Test Element";
        VirtualPatient virtualPatient = new VirtualPatient();

        Action action = new Action() {
            // This is an anonymous subclass of Action for testing purposes
        };
        action.setId(id);
        action.setPrimaryElement(primaryElement);
        action.setVirtualPatient(virtualPatient);

        assertEquals(id, action.getId());
        assertEquals(primaryElement, action.getPrimaryElement());
        assertEquals(virtualPatient, action.getVirtualPatient());
    }
}