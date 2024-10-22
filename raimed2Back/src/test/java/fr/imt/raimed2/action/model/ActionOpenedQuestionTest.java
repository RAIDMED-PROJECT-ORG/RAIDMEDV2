package fr.imt.raimed2.action.model;

import fr.imt.raimed2.question.model.Question;
import fr.imt.raimed2.virtualPatient.model.VirtualPatient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionOpenedQuestionTest {

    @Test
    void testActionOpenedQuestionFields() {
        String openedAnswer = "Test Answer";
        Question question = new Question();
        ActionOpenedQuestion actionOpenedQuestion = new ActionOpenedQuestion();
        actionOpenedQuestion.setOpenedAnswer(openedAnswer);
        actionOpenedQuestion.setQuestion(question);

        assertEquals(openedAnswer, actionOpenedQuestion.getOpenedAnswer());
        assertEquals(question, actionOpenedQuestion.getQuestion());
    }
}