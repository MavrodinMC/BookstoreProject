package com.mavro.services;

import com.mavro.dto.ShippingAddressDto;
import com.mavro.entities.AppUser;
import com.mavro.entities.ShippingAddress;
import com.mavro.exceptions.EmailNotFoundException;
import com.mavro.exceptions.ShippingAddressNotFoundException;
import com.mavro.repositories.AppUserRepository;
import com.mavro.repositories.ShippingAddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;
    private final AppUserRepository appUserRepository;

    public List<ShippingAddress> getAllAddressesForAUser(String email) {

        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(EmailNotFoundException::new);

        return new ArrayList<>(appUser.getShippingAddresses());

    }

   public void saveAddressForAUser(String email, ShippingAddressDto shippingAddressDto) {

        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(EmailNotFoundException::new);

        ShippingAddress shippingAddress = new ShippingAddress();

        shippingAddress.setFullName(shippingAddressDto.getFullName());
        shippingAddress.setAddress(shippingAddressDto.getAddress());
        shippingAddress.setPhoneNumber(shippingAddressDto.getPhoneNumber());
        shippingAddress.setCity(shippingAddressDto.getCity());
        shippingAddress.setState(shippingAddressDto.getState());
        shippingAddress.setZipCode(shippingAddressDto.getZipCode());
        shippingAddress.setIsDefault(shippingAddressDto.isDefault());

        shippingAddressRepository.save(shippingAddress);


        appUser.addAddress(shippingAddress);

        appUserRepository.save(appUser);
   }

   public void updateAnAddressForAUser(ShippingAddress shippingAddress) {

        shippingAddressRepository.save(shippingAddress);
   }

   public void deleteAnAddressForAUser(int shippingAddressId, String email) {

        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(EmailNotFoundException::new);

        ShippingAddress shippingAddress = shippingAddressRepository.findById(shippingAddressId)
                .orElseThrow(ShippingAddressNotFoundException::new);

        List<ShippingAddress> shippingAddresses = new ArrayList<>(appUser.getShippingAddresses());

        for (ShippingAddress shippingAddressToDelete: shippingAddresses) {

            if (shippingAddressToDelete.getAddressId().equals(shippingAddress.getAddressId())) {
                appUser.removeAddress(shippingAddressToDelete);
                shippingAddressRepository.deleteById(shippingAddressToDelete.getAddressId());
            }
        }
        appUserRepository.save(appUser);
   }

}
