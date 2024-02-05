package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.BadRequestException;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.NotFoundException;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.UnauthorizedException;
import antoniogiovanni.marchese.CapstoneBackend.model.Address;
import antoniogiovanni.marchese.CapstoneBackend.model.Student;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.model.enums.Role;
import antoniogiovanni.marchese.CapstoneBackend.payloads.StudentRegisterDTO;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserDTO;
import antoniogiovanni.marchese.CapstoneBackend.repository.AddressRepository;
import antoniogiovanni.marchese.CapstoneBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

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

    public User save(UserDTO userDTO){
        userRepository.findByEmail(userDTO.email()).ifPresent(utente -> {
            throw new BadRequestException("L'email "+utente.getEmail()+ " è già in uso!");
        });
        User newUser = new User();

        newUser.setEmail(userDTO.email());
        newUser.setPassword(bcrypt.encode(userDTO.password()));
        if(userDTO.role() != null){
            newUser.setRole(userDTO.role());
        }
        return userRepository.save(newUser);
    }

    public Student save(StudentRegisterDTO studentRegisterDTO){
        userRepository.findByEmail(studentRegisterDTO.email()).ifPresent(utente -> {
            throw new BadRequestException("L'email "+utente.getEmail()+ " è già in uso!");
        });
        Student newStudent = new Student();

        newStudent.setEmail(studentRegisterDTO.email());
        newStudent.setPassword(bcrypt.encode(studentRegisterDTO.password()));
        newStudent.setCf(studentRegisterDTO.cf());
        newStudent.setName(studentRegisterDTO.name());
        newStudent.setSurname(studentRegisterDTO.surname());
        //the role must be STUDENT
        if(studentRegisterDTO.role() != Role.STUDENT){
            throw new UnauthorizedException("the role must be student");
        }
        if(studentRegisterDTO.role() != null){
            newStudent.setRole(studentRegisterDTO.role());
        }
        Student student = userRepository.save(newStudent);
        //at this point we must save address
        Address address = new Address();
        address.setUser(userRepository.findById(student.getId()).orElseThrow(() -> new NotFoundException("student with ID: " + student.getId()+" not found")));
        address.setCity(studentRegisterDTO.city());
        address.setStreet(studentRegisterDTO.street());
        address.setProvince(studentRegisterDTO.province());
        address.setPostalCode(studentRegisterDTO.postalCode());
        address.setHouseNumber(studentRegisterDTO.houseNumber());
        addressService.save(address);

        return  student;
    }

    public User findById(UUID id){
        return userRepository.findById(id).orElseThrow(()->new NotFoundException(id));
    }

    public void findByIdAndDelete(UUID id){
        User found = this.findById(id);
        userRepository.delete(found);
    }

    public User findbyIdAndUpdate(UUID id, User body){
        User found = this.findById(id);
//        found.setCognome(body.getCognome());
//        found.setNome(body.getNome());
        found.setEmail(body.getEmail());
        return userRepository.save(found);

    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato!"));
    }


}
