package me.devld.tour.service.impl;

import me.devld.tour.dto.DistrictDetail;
import me.devld.tour.entity.District;
import me.devld.tour.repository.DistrictRepository;
import me.devld.tour.service.DistrictService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DistrictServiceImpl implements DistrictService {

    private final Map<Integer, DistrictDetail> districtMap;

    private final DistrictRepository districtRepository;

    public DistrictServiceImpl(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
        districtMap = buildTree(districtRepository.findAll());
    }

    private static Map<Integer, DistrictDetail> buildTree(List<District> all) {
        List<DistrictDetail> districtDetails = all.stream().map(DistrictDetail::new).collect(Collectors.toList());
        Map<Integer, DistrictDetail> districtMap = districtDetails.stream().collect(Collectors.toMap(e -> e.getDistrict().getId(), e -> e));
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
        return districtMap;
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

    @Override
    public List<District> getFlattingChildren(int id) {
        District district = getById(id);
        if (district == null) {
            return Collections.emptyList();
        }
        List<District> children = Collections.singletonList(district);
        List<District> result = new LinkedList<>(children);
        List<District> nextLevel;
        do {
            nextLevel = children.stream().map(e -> getChildren(e.getId())).flatMap(Collection::stream).collect(Collectors.toList());
            result.addAll(nextLevel);
            children = nextLevel;
        } while (!nextLevel.isEmpty());
        return result;
    }

    @Override
    public District getById(int id) {
        return districtMap.containsKey(id) ? districtMap.get(id).getDistrict() : null;
    }

    private List<DistrictDetail> resolveDistrict(int id) {
        DistrictDetail district;
        List<DistrictDetail> districtDetails = new ArrayList<>(3);
        while (id > 0 && (district = districtMap.get(id)) != null) {
            districtDetails.add(district);
            id = district.getDistrict().getParentId();
        }
        Collections.reverse(districtDetails);
        return districtDetails;
    }

    @Cacheable("tour_district")
    @Override
    public District getByCode(String code) {
        return districtRepository.findFirstByCode(code);
    }
}
