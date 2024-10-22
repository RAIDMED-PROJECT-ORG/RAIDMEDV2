package fr.imt.raimed2.question.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void getId() {
        Question question = new Question();
        Long id = 1L;
        question.setId(id);
        assertEquals(id, question.getId());
    }

    @Test
    void getType() {
        Question question = new Question();
        QuestionType type = QuestionType.OPENED;
        question.setType(type);
        assertEquals(type, question.getType());
    }

    @Test
    void getContent() {
        Question question = new Question();
        String content = "Test Content";
        question.setContent(content);
        assertEquals(content, question.getContent());
    }

    @Test
    void getFilter() {
        Question question = new Question();
        Filter filter = Filter.MALE;
        question.setFilter(filter);
        assertEquals(filter, question.getFilter());
    }

    @Test
    void setId() {
        Question question = new Question();
        Long id = 1L;
        question.setId(id);
        assertEquals(id, question.getId());
    }

    @Test
    void setType() {
        Question question = new Question();
        QuestionType type = QuestionType.OPENED;
        question.setType(type);
        assertEquals(type, question.getType());
    }

    @Test
    void setContent() {
        Question question = new Question();
        String content = "Test Content";
        question.setContent(content);
        assertEquals(content, question.getContent());
    }

    @Test
    void setFilter() {
        Question question = new Question();
        Filter filter = Filter.MALE;
        question.setFilter(filter);
        assertEquals(filter, question.getFilter());
    }

    @Test
    void builder() {
        Long id = 1L;
        QuestionType type = QuestionType.OPENED;
        String content = "Test Content";
        Filter filter = Filter.MALE;

        Question question = Question.builder()
                .id(id)
                .type(type)
                .content(content)
                .filter(filter)
                .build();

        assertEquals(id, question.getId());
        assertEquals(type, question.getType());
        assertEquals(content, question.getContent());
        assertEquals(filter, question.getFilter());
    }
}