package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.NotFoundException;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.UnauthorizedException;
import antoniogiovanni.marchese.CapstoneBackend.model.Subject;
import antoniogiovanni.marchese.CapstoneBackend.model.Teacher;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.payloads.SubjectDTO;
import antoniogiovanni.marchese.CapstoneBackend.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserService userService;

    public Subject findById(Long id){
        return subjectRepository.findById(id).orElseThrow(() -> new NotFoundException("subject with id " + id + " not found!"));
    }

    public Subject save(SubjectDTO subjectDTO, User currentUser){
        Teacher teacher = (Teacher) userService.findById(currentUser.getId());
        Subject subject = new Subject();
        subject.setName(subjectDTO.name());
        Subject subject1 = subjectRepository.save(subject);
        Subject subjectFromDB = this.findById(subject1.getId());
        teacher.getSubjectList().add(subjectFromDB);
        userService.save(teacher);
        subjectFromDB.getTeacherList().add(teacher);
        return subjectRepository.save(subjectFromDB);
    }
    public Subject addSubject(Long subjectId,User currentUser){
        Teacher teacher = (Teacher) userService.findById(currentUser.getId());
        Subject subject = this.findById(subjectId);
        teacher.getSubjectList().add(subject);
        userService.save(teacher);
        subject.getTeacherList().add(teacher);
        return subjectRepository.save(subject);
    }

    public List<Subject> getSubjectsByTeacher(Long idTeacher){
        return subjectRepository.findByTeacher(idTeacher);
    }

    public List<Subject> getSubjectsByNoTeacher(Long idTeacher){
        return subjectRepository.findByNoTeacher(idTeacher);
    }

    public Subject update(Long subjectId,SubjectDTO subjectDTO,User currentUser){
        List<Subject> subjectList = getSubjectsByTeacher(currentUser.getId());
        boolean ok = false;
        for(Subject subject: subjectList){
            if(subject.getId() == subjectId){
                ok = true;
            }
        }
        if(!ok){
            throw new UnauthorizedException("cannot modify others subjects");
        }
        Subject subjectFromDB = this.findById(subjectId);
        subjectFromDB.setName(subjectDTO.name());
        return subjectRepository.save(subjectFromDB);
    }

    public void findByIdAndDelete(Long subjectId,User currentUser){
        List<Subject> subjectList = getSubjectsByTeacher(currentUser.getId());
        boolean ok = false;
        for(Subject subject: subjectList){
            if(subject.getId() == subjectId){
                ok = true;
            }
        }
        if(!ok){
            throw new UnauthorizedException("cannot modify others subjects");
        }
        Subject subject = this.findById(subjectId);
        subjectRepository.delete(subject);
    }

    public List<Subject> findAll(){
        return subjectRepository.findAll();
    }
}
