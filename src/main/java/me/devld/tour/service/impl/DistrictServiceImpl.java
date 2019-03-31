package me.devld.tour.service.impl;

import me.devld.tour.dto.DistrictDetail;
import me.devld.tour.entity.District;
import me.devld.tour.repository.DistrictRepository;
import me.devld.tour.service.DistrictService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DistrictServiceImpl implements DistrictService {

    private Map<Integer, DistrictDetail> districtMap;

    public DistrictServiceImpl(DistrictRepository districtRepository) {
        buildTree(districtRepository.findAll());
    }

    private void buildTree(List<District> all) {
        List<DistrictDetail> districtDetails = all.stream().map(DistrictDetail::new).collect(Collectors.toList());
        districtMap = districtDetails.stream().collect(Collectors.toMap(e -> e.getDistrict().getId(), e -> e));
        DistrictDetail root = new DistrictDetail(new District());
        districtMap.put(0, root);
        for (DistrictDetail district : districtDetails) {
            DistrictDetail parent = districtMap.get(district.getDistrict().getParentId());
            parent.addChild(district);
            district.setParent(parent);
        }
        for (DistrictDetail district : districtDetails) {
            district.getChildren().sort(Comparator.comparingInt(a -> a.getDistrict().getOrder()));
        }
    }

    @Override
    public String getFullName(int id) {
        List<DistrictDetail> districts = resolveDistrict(id);
        StringBuilder s = new StringBuilder();
        for (DistrictDetail district : districts) {
            s.append(district.getDistrict().getName()).append(district.getDistrict().getExtra()).append(district.getDistrict().getSuffix());
        }
        return s.toString();
    }

    @Override
    public List<District> getFullDistrict(int id) {
        List<DistrictDetail> districts = resolveDistrict(id);
        List<District> result = new ArrayList<>(3);
        for (DistrictDetail detail : districts) {
            result.add(detail.getDistrict());
        }
        return result.isEmpty() ? null : result;
    }

    @Override
    public List<District> getChildren(int id) {
        List<DistrictDetail> temp = districtMap.containsKey(id) ? districtMap.get(id).getChildren() : Collections.emptyList();
        List<District> districts = new LinkedList<>();
        for (DistrictDetail detail : temp) {
            districts.add(detail.getDistrict());
        }
        return districts;
    }

    @Override
    public DistrictDetail getTree() {
        return districtMap.get(0);
    }

    private List<DistrictDetail> resolveDistrict(int id) {
        DistrictDetail district = null;
        List<DistrictDetail> districtDetails = new ArrayList<>(3);
        while (id > 0 && (district = districtMap.get(id)) != null) {
            districtDetails.add(district);
            id = district.getDistrict().getParentId();
        }
        Collections.reverse(districtDetails);
        return districtDetails;
    }
}
