package fr.imt.raimed2.diagnostic.model;

import fr.imt.raimed2.action.model.Action;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void testEventFields() {
        Diagnostic diagnostic = new Diagnostic();
        Action action = new Action() {};
        Date date = new Date();
        UUID id = UUID.randomUUID();

        Event event = new Event();
        event.setId(id);
        event.setDate(date);
        event.setDiagnostic(diagnostic);
        event.setAction(action);

        assertEquals(id, event.getId());
        assertEquals(date, event.getDate());
        assertEquals(diagnostic, event.getDiagnostic());
        assertEquals(action, event.getAction());
    }
}