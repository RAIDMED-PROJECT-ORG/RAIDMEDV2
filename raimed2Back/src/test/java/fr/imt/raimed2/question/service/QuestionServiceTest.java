package fr.imt.raimed2.question.service;

import fr.imt.raimed2.question.dto.request.CreateQuestionDto;
import fr.imt.raimed2.question.dto.request.UpdateQuestionDto;
import fr.imt.raimed2.question.dto.xml.QuestionLinkedDTO;
import fr.imt.raimed2.question.dto.xml.QuestionLinkedMapper;
import fr.imt.raimed2.question.model.Filter;
import fr.imt.raimed2.question.model.Question;
import fr.imt.raimed2.question.model.QuestionType;
import fr.imt.raimed2.question.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionLinkedMapper questionLinkedMapper;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void getAllQuestion() {
        List<Question> questions = List.of(new Question());
        when(questionRepository.findAll()).thenReturn(questions);

        List<Question> result = questionService.getAllQuestion(null);

        assertEquals(questions, result);
        verify(questionRepository, times(1)).findAll();
    }

    @Test
    void getAllQuestionByType() {
        QuestionType type = QuestionType.OPENED;
        List<Question> questions = List.of(new Question());
        when(questionRepository.findAllByType(type)).thenReturn(questions);

        List<Question> result = questionService.getAllQuestion(type);

        assertEquals(questions, result);
        verify(questionRepository, times(1)).findAllByType(type);
    }

    @Test
    void createQuestion() {
        CreateQuestionDto createQuestionDto = new CreateQuestionDto();
        createQuestionDto.setContent("Test Content");
        createQuestionDto.setFilter(Filter.MALE);
        createQuestionDto.setType(QuestionType.OPENED);

        Question question = new Question();
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        Question result = questionService.createQuestion(createQuestionDto);

        assertEquals(question, result);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void updateQuestion() {
        Long id = 1L;
        UpdateQuestionDto updateQuestionDto = new UpdateQuestionDto();
        updateQuestionDto.setContent("Updated Content");
        updateQuestionDto.setFilter(Filter.MALE);
        updateQuestionDto.setType(QuestionType.CLOSED);

        Question question = new Question();
        when(questionRepository.findById(id)).thenReturn(Optional.of(question));
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        Question result = questionService.updateQuestion(id, updateQuestionDto);

        assertEquals(question, result);
        verify(questionRepository, times(1)).findById(id);
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void updateQuestion_NotFound() {
        Long id = 1L;
        UpdateQuestionDto updateQuestionDto = new UpdateQuestionDto();

        when(questionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            questionService.updateQuestion(id, updateQuestionDto);
        });
    }

    @Test
    void deleteQuestion() {
        Long id = 1L;

        questionService.deleteQuestion(id);

        verify(questionRepository, times(1)).deleteById(id);
    }

    @Test
    void save() {
        QuestionLinkedDTO questionLinkedDTO = new QuestionLinkedDTO();
        questionLinkedDTO.setContent("Test Content");

        Question question = new Question();
        when(questionRepository.findAll()).thenReturn(List.of());
        when(questionRepository.save(any(Question.class))).thenReturn(question);
        when(questionLinkedMapper.questionLinkedDtoToQuestion(questionLinkedDTO)).thenReturn(question);

        Question result = questionService.save(questionLinkedDTO);

        assertEquals(question, result);
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void save_ExistingQuestion() {
        QuestionLinkedDTO questionLinkedDTO = new QuestionLinkedDTO();
        questionLinkedDTO.setContent("Test Content");

        Question existingQuestion = new Question();
        existingQuestion.setContent("Test Content");
        when(questionRepository.findAll()).thenReturn(List.of(existingQuestion));

        Question result = questionService.save(questionLinkedDTO);

        assertEquals(existingQuestion, result);
        verify(questionRepository, never()).save(any(Question.class));
    }
}