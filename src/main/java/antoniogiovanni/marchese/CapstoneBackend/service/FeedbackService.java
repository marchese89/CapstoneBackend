package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.UnauthorizedException;
import antoniogiovanni.marchese.CapstoneBackend.model.*;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.RequestState;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.SolutionState;
import antoniogiovanni.marchese.CapstoneBackend.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    public Feedback save(Student student, Long requestId,Integer score){

        Request request = requestService.findById(requestId);
        if(request.getRequestState() != RequestState.CLOSED){
            throw new UnauthorizedException("cannot send feedback for not closed requests");
        }
        if(request.getStudent().getId() != student.getId()){
            throw new UnauthorizedException("cannot send feedback for requests of others");
        }
        //check if feedback already exists
        Feedback feedback = null;
        boolean old = false;
        if(request.getFeedback() != null) {
            feedback = request.getFeedback();
            old = true;
        }else{
            feedback = new Feedback();
        }
        if(!old) {
            Solution solution = null;
            List<Solution> solutionList = request.getSolutionList();
            for (Solution s : solutionList) {
                if (s.getState() == SolutionState.ACCEPTED) {
                    solution = s;
                }
            }
            feedback.setRequest(request);
            feedback.setStudent(student);
            feedback.setTeacher(solution.getTeacher());
        }
        feedback.setScore(score);
        Feedback f = feedbackRepository.save(feedback);
        Teacher teacherFromDB = (Teacher) userService.findById(feedback.getTeacher().getId());
        teacherFromDB.setFeedback(requestService.getTeacherFeedback(feedback.getTeacher().getId()));
        userService.save(teacherFromDB);
        return f;

    }


}
