package fr.imt.raimed2.action.service;

import fr.imt.raimed2.action.dto.request.CreateActionSpontaneousPatientSpeech;
import fr.imt.raimed2.action.dto.xml.ActionDTO;
import fr.imt.raimed2.action.model.ActionClosedQuestion;
import fr.imt.raimed2.action.model.ActionOpenedQuestion;
import fr.imt.raimed2.action.model.ActionSpontaneousPatientSpeech;
import fr.imt.raimed2.action.repository.ActionClosedQuestionRepository;
import fr.imt.raimed2.action.repository.ActionOpenedQuestionRepository;
import fr.imt.raimed2.action.repository.ActionRepository;
import fr.imt.raimed2.question.model.Question;
import fr.imt.raimed2.virtualPatient.model.VirtualPatient;
import fr.imt.raimed2.virtualPatient.repository.VirtualPatientRepository;
import fr.imt.raimed2.question.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActionServiceTest {

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private ActionClosedQuestionRepository actionClosedQuestionRepository;

    @Mock
    private ActionOpenedQuestionRepository actionOpenedQuestionRepository;

    @Mock
    private VirtualPatientRepository virtualPatientRepository;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private ActionService actionService;

    @Test
    void addSpontaneousPatientSpeechAction_VirtualPatientExists() {
        Long virtualPatientId = 1L;
        CreateActionSpontaneousPatientSpeech createAction = new CreateActionSpontaneousPatientSpeech();
        createAction.setSpeech("Test Speech");
        createAction.setPrimaryElement("Test Element");

        VirtualPatient virtualPatient = new VirtualPatient();
        when(virtualPatientRepository.findById(virtualPatientId)).thenReturn(Optional.of(virtualPatient));

        actionService.addSpontaneousPatientSpeechAction(virtualPatientId, createAction);

        verify(actionRepository, times(1)).save(any(ActionSpontaneousPatientSpeech.class));
    }

    @Test
    void addSpontaneousPatientSpeechAction_VirtualPatientDoesNotExist() {
        Long virtualPatientId = 1L;
        CreateActionSpontaneousPatientSpeech createAction = new CreateActionSpontaneousPatientSpeech();

        when(virtualPatientRepository.findById(virtualPatientId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            actionService.addSpontaneousPatientSpeechAction(virtualPatientId, createAction);
        });
    }

    @Test
    void getAllClosedQuestionOfVirtualPatient() {
        Long virtualPatientId = 1L;
        List<ActionClosedQuestion> questions = List.of(new ActionClosedQuestion());
        when(actionClosedQuestionRepository.findAllByVirtualPatientId(virtualPatientId)).thenReturn(questions);

        List<ActionClosedQuestion> result = actionService.getAllClosedQuestionOfVirtualPatient(virtualPatientId);

        assertEquals(questions, result);
    }

    @Test
    void getAllOpenedQuestionOfVirtualPatient() {
        Long virtualPatientId = 1L;
        List<ActionOpenedQuestion> questions = List.of(new ActionOpenedQuestion());
        when(actionOpenedQuestionRepository.findAllByVirtualPatientId(virtualPatientId)).thenReturn(questions);

        List<ActionOpenedQuestion> result = actionService.getAllOpenedQuestionOfVirtualPatient(virtualPatientId);

        assertEquals(questions, result);
    }
}