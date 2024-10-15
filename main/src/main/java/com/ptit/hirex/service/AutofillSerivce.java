package com.ptit.hirex.service;

import com.ptit.data.entity.*;
import com.ptit.data.repository.*;
import com.ptit.hirex.dto.response.DistrictResponse;
import com.ptit.hirex.enums.StatusCodeEnum;
import com.ptit.hirex.model.ResponseBuilder;
import com.ptit.hirex.model.ResponseDto;
import com.ptit.hirex.util.Util;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutofillSerivce {

    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final LanguageService languageService;
    private final ModelMapper modelMapper;
    private final SalaryRepository salaryRepository;
    private final JobTypeRepository jobTypeRepository;
    private final TechRepository techRepository;
    private final YearExperienceRepository yearExperienceRepository;
    private final PositionRepository positionRepository;
    private final ContractTypeRepository contractTypeRepository;
    private final CompanyRepository companyRepository;


    public ResponseEntity<ResponseDto<List<City>>> autofillCity(String name) {
        try {
            List<City> cityList;

            if (Util.isNullOrEmpty(name)) {
                cityList = cityRepository.findAll();
            } else {
                cityList = cityRepository.findByNameContainingIgnoreCase(name);
            }

            return ResponseBuilder.okResponse(languageService.getMessage("autofill.city.success"),
                    cityList, StatusCodeEnum.CITY1000);
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(languageService.getMessage("autofill.city.failed"), StatusCodeEnum.CITY0000);
        }
    }

    public ResponseEntity<ResponseDto<List<DistrictResponse>>> autofillDistrict(String name, List<Long> cityIds) {
        try {
            List<District> districts;

            if (cityIds == null) {
                cityIds = new ArrayList<>();
            }

            if (Util.isNullOrEmpty(name) && cityIds.isEmpty()) {
                districts = districtRepository.findAll();
            } else if (!Util.isNullOrEmpty(name) && cityIds.isEmpty()) {
                districts = districtRepository.findByNameContainingIgnoreCase(name);
            } else if (Util.isNullOrEmpty(name) && !cityIds.isEmpty()) {
                districts = districtRepository.findByCityIdIn(cityIds);
            } else {
                districts = districtRepository.findByNameContainingIgnoreCaseAndCountryIds(name, cityIds);
            }

            List<DistrictResponse> districtResponses = new ArrayList<>();

            for (District district : districts) {
                districtResponses.add(modelMapper.map(district, DistrictResponse.class));
            }
            return ResponseBuilder.okResponse(languageService.getMessage("autofill.district.success"),
                    districtResponses, StatusCodeEnum.DISTRICT1000);
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(languageService.getMessage("autofill.district.failed"), StatusCodeEnum.DISTRICT0000);
        }
    }


    public ResponseEntity<ResponseDto<List<DistrictResponse>>> autofillDistrict(String name, Long cityId) {
        try {
            List<District> districts;

            if (Util.isNullOrEmpty(name)) {
                districts = districtRepository.findByCityId(cityId);
            } else {
                districts = districtRepository.findByNameContainingIgnoreCaseAndCityId(name, cityId);
            }

            List<DistrictResponse> districtResponses = new ArrayList<>();
            for (District district : districts) {
                districtResponses.add(modelMapper.map(district, DistrictResponse.class));
            }

            return ResponseBuilder.okResponse(
                    languageService.getMessage("autofill.district.success"),
                    districtResponses,
                    StatusCodeEnum.DISTRICT1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("autofill.district.failed"),
                    StatusCodeEnum.DISTRICT0000
            );
        }
    }

    public ResponseEntity<ResponseDto<List<Salary>>> autofillSalary() {
        try {
            List<Salary> salaries = salaryRepository.findAll();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("autofill.salary.success"),
                    salaries,
                    StatusCodeEnum.SALARY1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("autofill.salary.failed"),
                    StatusCodeEnum.SALARY0000
            );
        }
    }

    public ResponseEntity<ResponseDto<List<JobType>>> autofillJobType() {
        try {
            List<JobType> jobTypes = jobTypeRepository.findAll();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("autofill.job.type.success"),
                    jobTypes,
                    StatusCodeEnum.JOBTYPE1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("autofill.job.type.failed"),
                    StatusCodeEnum.JOBTYPE0000
            );
        }
    }

    public ResponseEntity<ResponseDto<List<Tech>>> autofillTech() {
        try {
            List<Tech> techList = techRepository.findAll();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("autofill.tech.success"),
                    techList,
                    StatusCodeEnum.TECH1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("autofill.tech.failed"),
                    StatusCodeEnum.TECH0000
            );
        }
    }

    public ResponseEntity<ResponseDto<List<YearExperience>>> autofillYearExperience() {
        try {
            List<YearExperience> experiences = yearExperienceRepository.findAll();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("autofill.year.experience.success"),
                    experiences,
                    StatusCodeEnum.YEAREXPERIENCE1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("autofill.year.experience.failed"),
                    StatusCodeEnum.YEAREXPERIENCE0000
            );
        }
    }

    public ResponseEntity<ResponseDto<List<Position>>> autofillPosition() {
        try {
            List<Position> positionList = positionRepository.findAll();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("autofill.position.success"),
                    positionList,
                    StatusCodeEnum.POSITION1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("autofill.position.failed"),
                    StatusCodeEnum.POSITION0000
            );
        }
    }

    public ResponseEntity<ResponseDto<List<ContractType>>> autofillContractType() {
        try {
            List<ContractType> contractTypes = contractTypeRepository.findAll();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("autofill.contract.type.success"),
                    contractTypes,
                    StatusCodeEnum.CONTRACTTYPE1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("autofill.contract.type.failed"),
                    StatusCodeEnum.CONTRACTTYPE0000
            );
        }
    }

    public ResponseEntity<ResponseDto<List<Company>>> autofillCompany() {
        try {
            List<Company> companies = companyRepository.findAll();

            return ResponseBuilder.okResponse(
                    languageService.getMessage("autofill.company.success"),
                    companies,
                    StatusCodeEnum.COMPANY1000
            );
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse(
                    languageService.getMessage("autofill.company.failed"),
                    StatusCodeEnum.COMPANY0000
            );
        }
    }

}
