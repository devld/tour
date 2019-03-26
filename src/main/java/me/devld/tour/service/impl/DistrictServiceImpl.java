package me.devld.tour.service.impl;

import me.devld.tour.entity.District;
import me.devld.tour.repository.DistrictRepository;
import me.devld.tour.service.DistrictService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;

    public DistrictServiceImpl(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    @Cacheable("district")
    @Override
    public String getFullName(int id) {
        List<District> districts = new ArrayList<>(3);
        resolveDistrict(id, districts);
        if (districts.isEmpty()) {
            return "";
        }
        StringBuilder s = new StringBuilder();
        for (int i = districts.size() - 1; i >= 0; i--) {
            District district = districts.get(i);
            s.append(district.getName()).append(district.getSuffix());
        }
        return s.toString();
    }

    private void resolveDistrict(int id, List<District> districts) {
        Optional<District> district = districtRepository.findById(id);
        if (district.isPresent()) {
            districts.add(district.get());
            if (district.get().getParentId() != 0) {
                resolveDistrict(district.get().getParentId(), districts);
            }
        }
    }

}
