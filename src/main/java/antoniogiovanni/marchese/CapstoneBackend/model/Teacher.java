package antoniogiovanni.marchese.CapstoneBackend.model;

import antoniogiovanni.marchese.CapstoneBackend.model.enums.RequestState;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.SolutionState;
import antoniogiovanni.marchese.CapstoneBackend.service.RequestService;
import antoniogiovanni.marchese.CapstoneBackend.service.SolutionService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Teacher extends User{

//    private String certificateUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teacher_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjectList = new ArrayList<>();
    private String cf;
    private String piva;
    private Double feedback;

    public Double getFeedback() {
        if(feedback != null)
            return feedback;
        else
            return 0.0;
    }
}
