package antoniogiovanni.marchese.CapstoneBackend.service;

import antoniogiovanni.marchese.CapstoneBackend.exceptions.NotFoundException;
import antoniogiovanni.marchese.CapstoneBackend.exceptions.UnauthorizedException;
import antoniogiovanni.marchese.CapstoneBackend.model.Address;
import antoniogiovanni.marchese.CapstoneBackend.model.User;
import antoniogiovanni.marchese.CapstoneBackend.payloads.AddressModifyDTO;
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

    public Address findByIdAndUpdate(Long id, AddressModifyDTO addressModifyDTO, User user){
        Address address = this.findById(id);
        if(address.getUser().getId() != user.getId()){
            throw new UnauthorizedException("cannot modify address of other user!");
        }
        address.setCity(addressModifyDTO.city());
        address.setStreet(addressModifyDTO.street());
        address.setHouseNumber(addressModifyDTO.houseNumber());
        address.setProvince(addressModifyDTO.province());
        address.setPostalCode(addressModifyDTO.postalCode());
        return addressRepository.save(address);
    }
}
