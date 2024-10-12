package com.ptit.hirex.service;

import com.ptit.data.entity.City;
import com.ptit.data.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public List<City> getAddresses() {
        List<City> cities = addressRepository.findAll();
        return cities;
    }
}
