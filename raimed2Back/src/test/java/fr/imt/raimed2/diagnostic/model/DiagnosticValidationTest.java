package fr.imt.raimed2.diagnostic.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiagnosticValidationTest {

    @Test
    void testDiagnosticValidationFields() {
        Hypothesis hypothesis = new Hypothesis();
        DiagnosticValidation diagnosticValidation = new DiagnosticValidation();
        diagnosticValidation.setDegreeOfCertainty(80);
        diagnosticValidation.setHypothesis(hypothesis);

        assertEquals(80, diagnosticValidation.getDegreeOfCertainty());
        assertEquals(hypothesis, diagnosticValidation.getHypothesis());
    }
}