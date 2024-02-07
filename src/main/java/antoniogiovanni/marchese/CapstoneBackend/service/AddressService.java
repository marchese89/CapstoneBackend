package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.NotFoundException;
import antoniogiovanni.marchese.CapstoneBackend.model.Address;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.payloads.AddressDTO;
import antoniogiovanni.marchese.CapstoneBackend.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address save(Address address){
       return addressRepository.save(address);
    }
    public Address findById(Long id){
        return addressRepository.findById(id).orElseThrow(() -> new NotFoundException("address with id " + id + " not found!"));
    }

    public Address update(AddressDTO addressDTO, User user){
        Address address = this.findById(user.getAddress().getId());
        address.setCity(addressDTO.city());
        address.setStreet(addressDTO.street());
        address.setHouseNumber(addressDTO.houseNumber());
        address.setProvince(addressDTO.province());
        address.setPostalCode(addressDTO.postalCode());
        return addressRepository.save(address);
    }
}
