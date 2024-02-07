package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.NotFoundException;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.UnauthorizedException;
import antoniogiovanni.marchese.CapstoneBackend.model.*;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.RequestState;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.SolutionState;
import antoniogiovanni.marchese.CapstoneBackend.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class SolutionService {

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserService userService;

    public Solution save(String filePath,Long requestId,Long price, Teacher teacher){
        Solution solution = new Solution();
        solution.setSolutionUrl(filePath);
        Request request = requestService.findById(requestId);
        boolean ok = false;
        List<Subject> subjectList = teacher.getSubjectList();
        for(Subject subject: subjectList){
            if(subject.equals(request.getSubject())){
                ok = true;
                break;
            }
        }
        if(!ok){
            throw new UnauthorizedException("cannot send solution for subjects that not are in your list!");
        }

        solution.setRequest(request);
        solution.setState(SolutionState.PENDING);
        solution.setTeacher((Teacher) userService.findById(teacher.getId()));
        solution.setPrice(price);
        return solutionRepository.save(solution);
    }

    public List<Solution> getSolutionsByRequestId(Long requestId, Student student){
        Request request = requestService.findById(requestId);
        if(request.getStudent().getId() != student.getId()){
            throw new UnauthorizedException("you can read only solutions of your requests");
        }
        return solutionRepository.getSolutionsByRequestId(requestId);
    }

    public Solution findById(Long id){
        return solutionRepository.findById(id).orElseThrow(()->new NotFoundException(id));
    }

    public Solution acceptSolution(Long solutionId, Student student){
        Solution solution = this.findById(solutionId);
        Request request = requestService.findById(solution.getRequest().getId());
        if(solution.getRequest().getStudent().getId() != student.getId()){
            throw new UnauthorizedException("you can accept only solutions of your requests");
        }
        //update solution url from solution to request
        request.setSolutionUrl(solution.getSolutionUrl());
        request.setRequestState(RequestState.CLOSED);
        List<Solution> solutionList = request.getSolutionList();
        Iterator<Solution> it = solutionList.iterator();
        while (it.hasNext()){
            Solution s = it.next();
            if(s.getId() == solutionId){
                s.setState(SolutionState.ACCEPTED);
            }else{
                s.setState(SolutionState.REJECTED);
            }
        }
        request.setSolutionList(solutionList);
        requestService.save(request);
        return solutionRepository.save(solution);
    }
}
