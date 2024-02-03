package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.BadRequestException;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.NotFoundException;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.payloads.UserDTO;
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
