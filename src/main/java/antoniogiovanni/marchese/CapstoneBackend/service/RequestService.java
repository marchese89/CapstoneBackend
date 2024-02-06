package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.model.Request;
import antoniogiovanni.marchese.CapstoneBackend.model.Student;
import antoniogiovanni.marchese.CapstoneBackend.model.Subject;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.RequestState;
import antoniogiovanni.marchese.CapstoneBackend.payloads.RequestDTO;
import antoniogiovanni.marchese.CapstoneBackend.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired UserService userService;

    @Autowired
    SubjectService subjectService;

    public Request save(String questionUrl, Student student, RequestDTO requestDTO){
        Request request = new Request();
        request.setQuestionUrl(questionUrl);
        request.setTitle(requestDTO.title());
        request.setSubject(subjectService.findById(requestDTO.subjectId()));
        request.setRequestState(RequestState.OPEN);
        request.setStudent((Student) userService.findById(student.getId()));
        return requestRepository.save(request);
    }

}
