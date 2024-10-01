package com.ptit.hirex.service.impl;

import com.ptit.data.entity.Address;
import com.ptit.data.repository.AddressRepository;
import com.ptit.hirex.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public List<Address> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses;
    }
}
