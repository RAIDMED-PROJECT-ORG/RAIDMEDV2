package fr.imt.raimed2.diagnostic.model;

import fr.imt.raimed2.action.model.Action;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HypothesisTest {

    @Test
    void testHypothesisFields() {
        Hypothesis hypothesis = new Hypothesis();
        hypothesis.setValue("Test Value");
        hypothesis.setActions(Set.of(new Action() {}));
        hypothesis.setInterpretations(Set.of(new Interpretation()));
        hypothesis.setSyndroms(Set.of(new Syndrom()));

        assertEquals("Test Value", hypothesis.getValue());
        assertNotNull(hypothesis.getActions());
        assertNotNull(hypothesis.getInterpretations());
        assertNotNull(hypothesis.getSyndroms());
    }
}