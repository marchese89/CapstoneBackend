package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.BadRequestException;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.NotFoundException;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.UnauthorizedException;
import antoniogiovanni.marchese.CapstoneBackend.model.Address;
import antoniogiovanni.marchese.CapstoneBackend.model.Student;
import antoniogiovanni.marchese.CapstoneBackend.model.Teacher;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import antoniogiovanni.marchese.CapstoneBackend.payloads.PasswordDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserModifyDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserRegisterDTO;
import antoniogiovanni.marchese.CapstoneBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    AddressService addressService;

    private PasswordEncoder bcrypt =  new BCryptPasswordEncoder(11);
    public Page<User> getUsers(int page, int size, String orderBy){
        if (size >= 100) size = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return userRepository.findAll(pageable);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User save(UserRegisterDTO userRegisterDTO){
        userRepository.findByEmail(userRegisterDTO.email()).ifPresent(utente -> {
            throw new BadRequestException("L'email "+utente.getEmail()+ " è già in uso!");
        });

        if(userRegisterDTO.role() == Role.STUDENT){
            Student newStudent = new Student();
            newStudent.setEmail(userRegisterDTO.email());
            newStudent.setPassword(bcrypt.encode(userRegisterDTO.password()));
            newStudent.setCf(userRegisterDTO.cf());
            newStudent.setName(userRegisterDTO.name());
            newStudent.setSurname(userRegisterDTO.surname());
            newStudent.setRole(userRegisterDTO.role());
            Student student = userRepository.save(newStudent);
            //at this point we must save address
            Address address = new Address();
            address.setUser(userRepository.findById(student.getId()).orElseThrow(() -> new NotFoundException("student with ID: " + student.getId()+" not found")));
            address.setCity(userRegisterDTO.city());
            address.setStreet(userRegisterDTO.street());
            address.setProvince(userRegisterDTO.province());
            address.setPostalCode(userRegisterDTO.postalCode());
            address.setHouseNumber(userRegisterDTO.houseNumber());
            addressService.save(address);

            return  student;
        }
        if (userRegisterDTO.role() == Role.TEACHER){
            Teacher newTeacher = new Teacher();
            newTeacher.setEmail(userRegisterDTO.email());
            newTeacher.setPassword(bcrypt.encode(userRegisterDTO.password()));
            newTeacher.setCf(userRegisterDTO.cf());
            newTeacher.setName(userRegisterDTO.name());
            newTeacher.setSurname(userRegisterDTO.surname());
            newTeacher.setRole(userRegisterDTO.role());
            newTeacher.setPiva(userRegisterDTO.piva());
            Teacher teacher = userRepository.save(newTeacher);
            //at this point we must save address
            Address address = new Address();
            address.setUser(userRepository.findById(teacher.getId()).orElseThrow(() -> new NotFoundException("student with ID: " + teacher.getId()+" not found")));
            address.setCity(userRegisterDTO.city());
            address.setStreet(userRegisterDTO.street());
            address.setProvince(userRegisterDTO.province());
            address.setPostalCode(userRegisterDTO.postalCode());
            address.setHouseNumber(userRegisterDTO.houseNumber());
            addressService.save(address);

            return  teacher;
        }
        throw new UnauthorizedException("you can register only students or teachers");

    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(()->new NotFoundException(id));
    }

    public void findByIdAndDelete(Long id){
        User found = this.findById(id);
        userRepository.delete(found);
    }

    public Student update( UserModifyDTO userModifyDTO, User user){
        Student found = (Student) this.findById(user.getId());
        found.setName(userModifyDTO.name());
        found.setSurname(userModifyDTO.surname());
        found.setEmail(userModifyDTO.email());
        found.setCf(userModifyDTO.cf());
        return userRepository.save(found);

    }

    public User updatePassword(PasswordDTO passwordDTO, User user){
        User user1 = this.findById(user.getId());
        user1.setPassword(bcrypt.encode(passwordDTO.password()));
        return userRepository.save(user1);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("user with email " + email + " not found!"));
    }


}
