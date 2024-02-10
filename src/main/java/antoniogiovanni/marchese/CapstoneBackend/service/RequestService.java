package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.NotFoundException;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.UnauthorizedException;
import antoniogiovanni.marchese.CapstoneBackend.model.*;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.RequestState;
import antoniogiovanni.marchese.CapstoneBackend.payloads.RequestDTO;
import antoniogiovanni.marchese.CapstoneBackend.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {

    @Autowired
    private
    RequestRepository requestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private EmailService emailService;

    public Request findById(Long id){
       return requestRepository.findById(id).orElseThrow(()->new NotFoundException(id));
    }

    public Request save(Request request){
        return requestRepository.save(request);
    }

    public Request save(String questionUrl, Student student, RequestDTO requestDTO){
        Request request = new Request();
        request.setQuestionUrl(questionUrl);
        request.setTitle(requestDTO.title());
        Subject subject = subjectService.findById(requestDTO.subjectId());
        request.setSubject(subject);
        request.setRequestState(RequestState.OPEN);
        request.setStudent((Student) userService.findById(student.getId()));
        //send email to teachers
        subject.getTeacherList().stream().forEach(teacher -> emailService.sendEmail(teacher.getEmail(),"Nuova Richiesta","Salve,\nhai ricevuto una nuova richiesta\n\ncontrolla il tuo profilo."));
        return requestRepository.save(request);
    }
    public List<Request>getRequestByTeacher(Long idTeacher){
        return requestRepository.getRequestByTeacher(idTeacher);
    }

    public List<Request> getRequestByStudent(Long idStudent){
        return requestRepository.getRequestByStudent(idStudent);
    }

    public Request getByIdForStudents(Long idRequest, User currentUser){
        Request request = this.findById(idRequest);
        if(request.getStudent().getId() != currentUser.getId()){
            throw new UnauthorizedException("cannot read requests of other students");
        }
        return request;
    }
    public Request getByIdForTeachers(Long idRequest, User currentUser){
        Request request = this.findById(idRequest);
        boolean ok = false;
        List<Teacher> teacherList = request.getSubject().getTeacherList();
        for(Teacher teacher: teacherList){
            if(teacher.getId() == currentUser.getId()){
                ok = true;
            }
        }
        if(!ok){
            throw new UnauthorizedException("cannot read requests of other teachers");
        }
        return request;
    }
}
