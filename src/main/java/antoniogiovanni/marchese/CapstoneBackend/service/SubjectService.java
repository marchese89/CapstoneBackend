package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.NotFoundException;
import antoniogiovanni.marchese.CapstoneBackend.model.Subject;
import antoniogiovanni.marchese.CapstoneBackend.model.Teacher;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.payloads.SubjectDTO;
import antoniogiovanni.marchese.CapstoneBackend.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
