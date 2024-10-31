package fr.imt.raimed2.diagnostic.service;

import fr.imt.raimed2.action.model.Action;
import fr.imt.raimed2.action.model.ActionClosedQuestion;
import fr.imt.raimed2.action.model.ActionOpenedQuestion;
import fr.imt.raimed2.action.service.ActionService;
import fr.imt.raimed2.diagnostic.dto.request.CreateEventDto;
import fr.imt.raimed2.diagnostic.model.*;
import fr.imt.raimed2.diagnostic.repository.*;
import fr.imt.raimed2.question.model.Question;
import fr.imt.raimed2.question.model.QuestionType;
import fr.imt.raimed2.question.service.QuestionService;
import fr.imt.raimed2.user.model.Role;
import fr.imt.raimed2.user.model.User;
import fr.imt.raimed2.virtualPatient.model.VirtualPatient;
import fr.imt.raimed2.virtualPatient.repository.VirtualPatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiagnosticServiceTest {

    @Mock
    private DiagnosticRepository diagnosticRepository;

    @Mock
    private VirtualPatientRepository virtualPatientRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private InterpretationRepository interpretationRepository;

    @Mock
    private SyndromRepository syndromRepository;

    @Mock
    private HypothesisRepository hypothesisRepository;

    @Mock
    private QuestionService questionService;

    @Mock
    private ActionService actionService;

    @InjectMocks
    private DiagnosticService diagnosticService;

    @Test
    void getAllDiagnosticByUser() {
        User user = new User();

        List<Diagnostic> diagnostics = List.of(new Diagnostic());

        when(diagnosticRepository.findAllByUser(user)).thenReturn(diagnostics);

        List<Diagnostic> result = diagnosticService.getAllDiagnosticByUser(user);

        assertEquals(diagnostics, result);
        verify(diagnosticRepository, times(1)).findAllByUser(user);
    }

    @Test
    void getDiagnosticById() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setRole(Role.STUDENT);

        Long id = 1L;
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setUser(user);

        when(diagnosticRepository.findById(id)).thenReturn(Optional.of(diagnostic));

        Diagnostic result = diagnosticService.getDiagnosticById(id, user);

        assertEquals(diagnostic, result);
        verify(diagnosticRepository, times(1)).findById(id);
    }

    @Test
    void getDiagnosticById_AccessDenied() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setRole(Role.STUDENT);

        Long id = 1L;
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setId(id);

        User wrongUser = new User();
        diagnostic.setUser(wrongUser);
        diagnostic.getUser().setId(UUID.randomUUID());
        when(diagnosticRepository.findById(id)).thenReturn(Optional.of(diagnostic));

        assertThrows(AccessDeniedException.class, () -> {
            diagnosticService.getDiagnosticById(id, user);
        });
    }

    @Test
    void getAllEventByDiagnosticId() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setRole(Role.STUDENT);

        Long diagnosticId = 1L;
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setUser(user);
        diagnostic.setId(diagnosticId);

        List<Event> events = List.of(new Event());

        when(diagnosticRepository.findById(diagnosticId)).thenReturn(Optional.of(diagnostic));
        when(eventRepository.findAllByDiagnosticId(diagnosticId)).thenReturn(events);

        List<Event> result = diagnosticService.getAllEventByDiagnosticId(diagnosticId, user);

        assertEquals(events, result);
        verify(eventRepository, times(1)).findAllByDiagnosticId(diagnosticId);
    }

    @Test
    void getAllEventByDiagnosticId_AccessDenied() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setRole(Role.STUDENT);

        Long diagnosticId = 1L;
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setUser(user);
        diagnostic.setId(diagnosticId);

        User otherUser = new User();
        diagnostic.setUser(otherUser);
        diagnostic.getUser().setId(UUID.randomUUID());

        when(diagnosticRepository.findById(diagnosticId)).thenReturn(Optional.of(diagnostic));

        assertThrows(AccessDeniedException.class, () -> {
            diagnosticService.getAllEventByDiagnosticId(diagnosticId, user);
        });
    }

    @Test
    void getAllInterpretationByDiagnosticId() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setRole(Role.STUDENT);

        Long diagnosticId = 1L;
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setUser(user);
        diagnostic.setId(diagnosticId);

        List<Interpretation> interpretations = List.of(new Interpretation());
        when(diagnosticRepository.findById(diagnosticId)).thenReturn(Optional.of(diagnostic));
        when(interpretationRepository.findAllByDiagnosticId(diagnosticId)).thenReturn(interpretations);

        List<Interpretation> result = diagnosticService.getAllInterpretationByDiagnosticId(diagnosticId, user);

        assertEquals(interpretations, result);
        verify(interpretationRepository, times(1)).findAllByDiagnosticId(diagnosticId);
    }

    @Test
    void getAllInterpretationByDiagnosticId_AccessDenied() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setRole(Role.STUDENT);

        Long diagnosticId = 1L;
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setUser(new User());
        diagnostic.getUser().setId(UUID.randomUUID());

        when(diagnosticRepository.findById(diagnosticId)).thenReturn(Optional.of(diagnostic));

        assertThrows(AccessDeniedException.class, () -> {
            diagnosticService.getAllInterpretationByDiagnosticId(diagnosticId, user);
        });
    }

    @Test
    void getAllSyndromsByDiagnosticId() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setRole(Role.STUDENT);

        Long diagnosticId = 1L;
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setUser(user);
        diagnostic.setId(diagnosticId);

        List<Syndrom> syndroms = List.of(new Syndrom());

        when(diagnosticRepository.findById(diagnosticId)).thenReturn(Optional.of(diagnostic));
        when(syndromRepository.findAllByDiagnosticId(diagnosticId)).thenReturn(syndroms);

        List<Syndrom> result = diagnosticService.getAllSyndromsByDiagnosticId(diagnosticId, user);

        assertEquals(syndroms, result);
        verify(syndromRepository, times(1)).findAllByDiagnosticId(diagnosticId);
    }

    @Test
    void getAllSyndromsByDiagnosticId_AccessDenied() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setRole(Role.STUDENT);

        Long diagnosticId = 1L;
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setUser(new User());
        diagnostic.getUser().setId(UUID.randomUUID());

        when(diagnosticRepository.findById(diagnosticId)).thenReturn(Optional.of(diagnostic));

        assertThrows(AccessDeniedException.class, () -> {
            diagnosticService.getAllSyndromsByDiagnosticId(diagnosticId, user);
        });
    }

    @Test
    void getAllHypothesisByDiagnosticId() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setRole(Role.STUDENT);

        Long diagnosticId = 1L;
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setUser(user);
        diagnostic.setId(diagnosticId);

        List<Hypothesis> hypotheses = List.of(new Hypothesis());

        when(diagnosticRepository.findById(diagnosticId)).thenReturn(Optional.of(diagnostic));
        when(hypothesisRepository.findAllByDiagnosticId(diagnosticId)).thenReturn(hypotheses);

        List<Hypothesis> result = diagnosticService.getAllHypothesisByDiagnosticId(diagnosticId, user);

        assertEquals(hypotheses, result);
        verify(hypothesisRepository, times(1)).findAllByDiagnosticId(diagnosticId);
    }

    @Test
    void getAllHypothesisByDiagnosticId_AccessDenied() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setRole(Role.STUDENT);

        Long diagnosticId = 1L;
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setUser(new User());
        diagnostic.setId(diagnosticId);

        User otherUser = new User();
        diagnostic.setUser(otherUser);
        diagnostic.getUser().setId(UUID.randomUUID());


        when(diagnosticRepository.findById(diagnosticId)).thenReturn(Optional.of(diagnostic));

        assertThrows(AccessDeniedException.class, () -> {
            diagnosticService.getAllHypothesisByDiagnosticId(diagnosticId, user);
        });
    }

    @Test
    void getDiagnosticClosedQuestion() {
        VirtualPatient virtualPatient = new VirtualPatient();

        Diagnostic diagnostic = new Diagnostic();
        Long diagnosticId = 1L;
        diagnostic.setId(diagnosticId);
        diagnostic.setVirtualPatient(virtualPatient);

        List<Question> questions = new ArrayList<>(List.of(new Question()));
        List<ActionClosedQuestion> closedQuestions = new ArrayList<>(List.of(new ActionClosedQuestion()));
        List<ActionClosedQuestion> askedCloseQuestions = new ArrayList<>(List.of());

        when(diagnosticRepository.findById(diagnosticId)).thenReturn(Optional.of(diagnostic));
        when(questionService.getAllQuestion(QuestionType.CLOSED)).thenReturn(questions);
        when(actionService.getAllClosedQuestionOfVirtualPatient(virtualPatient.getId())).thenReturn(closedQuestions);
        when(actionService.getAllClosedQuestionOfDiagnosticEvents(eventRepository.findAllByDiagnosticId(diagnosticId))).thenReturn(askedCloseQuestions);

        List<Question> result = diagnosticService.getDiagnosticClosedQuestion(diagnosticId);

        assertEquals(questions, result);
    }

    @Test
    void getDiagnosticOpenedQuestion() {
        Long diagnosticId = 1L;
        Diagnostic diagnostic = new Diagnostic();
        VirtualPatient virtualPatient = new VirtualPatient();
        diagnostic.setVirtualPatient(virtualPatient);

        Question question = new Question();
        question.setId(1L);

        ActionOpenedQuestion openedQuestion = new ActionOpenedQuestion();
        openedQuestion.setQuestion(question);

        List<Question> questions = new ArrayList<>(List.of(question));
        List<ActionOpenedQuestion> openedQuestions = List.of(openedQuestion);
        List<ActionOpenedQuestion> askedOpenedQuestions = List.of();

        when(diagnosticRepository.findById(diagnosticId)).thenReturn(Optional.of(diagnostic));
        when(questionService.getAllQuestion(QuestionType.OPENED)).thenReturn(questions);
        when(actionService.getAllOpenedQuestionOfVirtualPatient(virtualPatient.getId())).thenReturn(openedQuestions);
        when(actionService.getAllOpenedQuestionOfDiagnosticEvents(eventRepository.findAllByDiagnosticId(diagnosticId))).thenReturn(askedOpenedQuestions);

        List<Question> result = diagnosticService.getDiagnosticOpenedQuestion(diagnosticId);

        assertEquals(questions, result);
    }

    @Test
    void startDiagnosticOfVirtualPatient() {
        User user = new User();

        Long virtualPatientId = 1L;
        VirtualPatient virtualPatient = new VirtualPatient();

        when(virtualPatientRepository.findById(virtualPatientId)).thenReturn(Optional.of(virtualPatient));

        Diagnostic diagnostic = new Diagnostic();
        when(diagnosticRepository.save(any(Diagnostic.class))).thenReturn(diagnostic);

        Diagnostic result = diagnosticService.startDiagnosticOfVirtualPatient(user, virtualPatientId);

        assertEquals(diagnostic, result);
        verify(diagnosticRepository, times(1)).save(any(Diagnostic.class));
    }

    @Test
    void addEventToDiagnostic() {
        CreateEventDto createEventDto = new CreateEventDto();
        createEventDto.setActionId(UUID.randomUUID());

        VirtualPatient virtualPatient = new VirtualPatient();

        Action action = new Action() {};
        action.setId(createEventDto.getActionId());
        virtualPatient.setActions(Set.of(action));

        Long diagnosticId = 1L;
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setVirtualPatient(virtualPatient);

        when(diagnosticRepository.findById(diagnosticId)).thenReturn(Optional.of(diagnostic));
        when(eventRepository.save(any(Event.class))).thenReturn(new Event());
        when(eventRepository.findAllByDiagnosticId(diagnosticId)).thenReturn(List.of(new Event()));

        List<Event> result = diagnosticService.addEventToDiagnostic(diagnosticId, createEventDto);

        assertNotNull(result);
        verify(eventRepository, times(1)).save(any(Event.class));
    }
}