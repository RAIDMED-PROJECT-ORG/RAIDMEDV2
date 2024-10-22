package fr.imt.raimed2.action.model;

import fr.imt.raimed2.virtualPatient.model.VirtualPatient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionSpontaneousPatientSpeechTest {

    @Test
    void testActionSpontaneousPatientSpeechFields() {
        String speech = "Test Speech";
        ActionSpontaneousPatientSpeech actionSpontaneousPatientSpeech = new ActionSpontaneousPatientSpeech();
        actionSpontaneousPatientSpeech.setSpeech(speech);

        assertEquals(speech, actionSpontaneousPatientSpeech.getSpeech());
    }
}