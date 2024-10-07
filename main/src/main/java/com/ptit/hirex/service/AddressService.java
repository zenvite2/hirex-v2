package com.ptit.hirex.service;

import com.ptit.data.entity.Address;
import com.ptit.data.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public List<Address> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses;
    }
}
