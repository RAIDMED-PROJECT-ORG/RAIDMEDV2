package fr.imt.raimed2.action.model;

import fr.imt.raimed2.question.model.Question;
import fr.imt.raimed2.virtualPatient.model.VirtualPatient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionClosedQuestionTest {

    @Test
    void testActionClosedQuestionFields() {
        String closedAnswer = "Test Answer";
        Question question = new Question();
        ActionClosedQuestion actionClosedQuestion = new ActionClosedQuestion();
        actionClosedQuestion.setClosedAnswer(closedAnswer);
        actionClosedQuestion.setQuestion(question);

        assertEquals(closedAnswer, actionClosedQuestion.getClosedAnswer());
        assertEquals(question, actionClosedQuestion.getQuestion());
    }
}