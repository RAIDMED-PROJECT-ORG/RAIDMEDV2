package fr.imt.raimed2.question.service;

import fr.imt.raimed2.action.dto.xml.ActionDtoMapper;
import fr.imt.raimed2.question.dto.request.CreateQuestionDto;
import fr.imt.raimed2.question.dto.request.UpdateQuestionDto;
import fr.imt.raimed2.question.dto.xml.QuestionLinkedDTO;
import fr.imt.raimed2.question.dto.xml.QuestionLinkedMapper;
import fr.imt.raimed2.question.model.Question;
import fr.imt.raimed2.question.model.QuestionType;
import fr.imt.raimed2.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    private final QuestionLinkedMapper questionLinkedMapper;
    private final ActionDtoMapper actionDtoMapper;

    /**
     * Get all the questions
     * @param questionType The type of the question
     * @return The list of questions
     */
    public List<Question> getAllQuestion(@Nullable QuestionType questionType) {
        if (questionType == null) {
            return questionRepository.findAll();
        }
        return questionRepository.findAllByType(questionType);
    }

    /**
     * Create a question and save it in the database
     * @param createQuestionDto Dto containing the information of the question
     * @return The created question
     */
    public Question createQuestion(CreateQuestionDto createQuestionDto) {
        Question createQuestion = Question.builder()
                .filter(createQuestionDto.getFilter())
                .content(createQuestionDto.getContent())
                .type(createQuestionDto.getType())
                .build();
        return questionRepository.save(createQuestion);
    }

    /**
     * Update a question
     * @param id The id of the question
     * @param updateQuestionDto Dto containing the information of the question
     * @return The updated question
     * @throws NoSuchElementException If the question does not exist
     */
    public Question updateQuestion(Long id, UpdateQuestionDto updateQuestionDto) throws NoSuchElementException {
        Question question = questionRepository.findById(id).orElseThrow();
        question.setFilter(updateQuestionDto.getFilter());
        question.setContent(updateQuestionDto.getContent());
        question.setType(updateQuestionDto.getType());
        return questionRepository.save(question);
    }

    /**
     * Delete a question
     * @param id The id of the question
     */
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    /**
     * Save a question
     * @param actionClosedQuestionsDTO The DTO object corresponding to the question
     * @return The question saved
     */
    public Question save(QuestionLinkedDTO questionLinkedDTO){
        // If QuestionLinked already exist in the question then we don't create it, we reuse it
        List<Question> questions = this.getAllQuestion(null);

        boolean isAlreadyInTheQuestions = questions.stream().anyMatch(n -> n.getContent().equals(questionLinkedDTO.getContent()));
        Question questionToSave = null;

        if (!isAlreadyInTheQuestions){
            questionToSave = questionRepository.save(questionLinkedMapper.questionLinkedDtoToQuestion(questionLinkedDTO));
        }else {
            // We have to link the existing question to the new ActionClosedQuestion
            Optional<Question> questionAlreadyExisting = questions.stream().filter(n -> n.getContent().equals(questionLinkedDTO.getContent())).findFirst();
            if(questionAlreadyExisting.isPresent()){
                questionToSave = questionAlreadyExisting.get();
            }
        }
        return questionToSave;
    }

}
