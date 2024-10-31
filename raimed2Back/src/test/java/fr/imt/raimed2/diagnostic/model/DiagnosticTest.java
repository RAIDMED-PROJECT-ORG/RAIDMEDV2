package fr.imt.raimed2.diagnostic.model;

import fr.imt.raimed2.user.model.User;
import fr.imt.raimed2.virtualPatient.model.VirtualPatient;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DiagnosticTest {

    @Test
    void testDiagnosticFields() {
        User user = new User();
        VirtualPatient virtualPatient = new VirtualPatient();
        Date startDate = new Date();

        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setId(1L);
        diagnostic.setUser(user);
        diagnostic.setStatus(DiagnosticStatus.IN_PROGRESS);
        diagnostic.setVirtualPatient(virtualPatient);
        diagnostic.setStartDate(startDate);

        assertEquals(1L, diagnostic.getId());
        assertEquals(user, diagnostic.getUser());
        assertEquals(DiagnosticStatus.IN_PROGRESS, diagnostic.getStatus());
        assertEquals(virtualPatient, diagnostic.getVirtualPatient());
        assertEquals(startDate, diagnostic.getStartDate());
    }
}