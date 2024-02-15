package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.NotFoundException;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.UnauthorizedException;
import antoniogiovanni.marchese.CapstoneBackend.model.*;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.RequestState;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.SolutionState;
import antoniogiovanni.marchese.CapstoneBackend.payloads.RequestDTO;
import antoniogiovanni.marchese.CapstoneBackend.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
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
        request.setDate(LocalDateTime.now());
        //send email to teachers
        subject.getTeacherList().stream().forEach(
                teacher -> emailService.sendEmail(
                        teacher.getEmail(),
                        "Nuova Richiesta",
                        "Salve,\nhai ricevuto una nuova richiesta\n\ncontrolla il tuo profilo."));
        return requestRepository.save(request);
    }

    public Page<Request> getRequestByTeacher(Long idTeacher,int page, int size, String orderBy,String direction){
        Sort.Direction sortDirection = Sort.Direction.DESC; // Default: decrescente

        if (direction != null && direction.equalsIgnoreCase("asc")) {
            sortDirection = Sort.Direction.ASC;
        }
        Sort sort = Sort.by(sortDirection, orderBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return requestRepository.getRequestByTeacher(idTeacher,pageable);
    }

    public List<Request> getRequestByTeacher(Long idTeacher){
        return requestRepository.getRequestByTeacher(idTeacher);
    }

//    public List<Request> getRequestByStudent(Long idStudent){
//        return requestRepository.getRequestByStudent(idStudent);
//    }

    public Page<Request> getRequestByStudent(Long idStudent,int page, int size, String orderBy,String direction){
        Sort.Direction sortDirection = Sort.Direction.DESC; // Default: decrescente

        if (direction != null && direction.equalsIgnoreCase("asc")) {
            sortDirection = Sort.Direction.ASC;
        }
        Sort sort = Sort.by(sortDirection, orderBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return requestRepository.getRequestByStudent(idStudent,pageable);
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

    public Double getTeacherFeedback(Long idTeacher){
        DecimalFormat df = new DecimalFormat("0.00");
        double total = 0;
        int number = 0;
        List<Request> requestList = this.getRequestByTeacher(idTeacher);
        for (Request request: requestList){
            if(request.getRequestState() == RequestState.CLOSED){
                List<Solution> solutionList = request.getSolutionList();
                for (Solution solution: solutionList){
                    if(solution.getState() == SolutionState.ACCEPTED && solution.getTeacher().getId() == idTeacher){
                        if(request.getFeedback() != null) {
                            total += request.getFeedback().getScore();
                            number++;
                        }
                    }
                }
            }
        }
        return Double.parseDouble(number > 0 ? df.format(total/number).replace(",","."):"0.0");
    }
}
