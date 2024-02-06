package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.NotFoundException;
import antoniogiovanni.marchese.CapstoneBackend.model.Subject;
import antoniogiovanni.marchese.CapstoneBackend.payloads.SubjectDTO;
import antoniogiovanni.marchese.CapstoneBackend.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public Subject findById(Long id){
        return subjectRepository.findById(id).orElseThrow(() -> new NotFoundException("subject with id " + id + " not found!"));
    }

    public Subject save(SubjectDTO subjectDTO){
        Subject subject = new Subject();
        subject.setName(subjectDTO.name());
        return subjectRepository.save(subject);
    }
}
