package me.devld.tour.service.impl;

import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.spot.SpotDestination;
import me.devld.tour.dto.spot.SpotDetailsOut;
import me.devld.tour.dto.spot.SpotIn;
import me.devld.tour.entity.*;
import me.devld.tour.entity.rel.LikeCollectRel;
import me.devld.tour.entity.rel.RelObjectType;
import me.devld.tour.entity.rel.RelType;
import me.devld.tour.exception.ForbiddenException;
import me.devld.tour.exception.NotFoundException;
import me.devld.tour.repository.SpotPhotoRepository;
import me.devld.tour.repository.SpotRepository;
import me.devld.tour.repository.SpotTicketRepository;
import me.devld.tour.service.DistrictService;
import me.devld.tour.service.LikeCollectService;
import me.devld.tour.service.SpotService;
import me.devld.tour.util.BeanUtil;
import me.devld.tour.util.HtmlUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpotServiceImpl implements SpotService {

    private final SpotRepository spotRepository;
    private final SpotTicketRepository spotTicketRepository;
    private final SpotPhotoRepository spotPhotoRepository;

    private final DistrictService districtService;
    private final LikeCollectService likeCollectService;

    public SpotServiceImpl(SpotRepository spotRepository,
                           SpotTicketRepository spotTicketRepository,
                           SpotPhotoRepository spotPhotoRepository,
                           DistrictService districtService,
                           LikeCollectService likeCollectService) {
        this.spotRepository = spotRepository;
        this.spotTicketRepository = spotTicketRepository;
        this.spotPhotoRepository = spotPhotoRepository;
        this.districtService = districtService;
        this.likeCollectService = likeCollectService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Spot createSpot(SpotIn spotIn) {
        Spot spot = new Spot();
        BeanUtils.copyProperties(spotIn, spot);
        spot.setIntro(HtmlUtils.sanitizer(spot.getIntro()));
        spotRepository.save(spot);

        List<SpotTicket> tickets = spotIn.getTickets();
        if (tickets != null && tickets.size() > 0) {
            tickets.forEach(e -> e.setSpotId(spot.getId()));
            spotTicketRepository.saveAll(tickets);
        }

        return spot;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Spot updateSpot(long spotId, SpotIn spotIn) {
        Spot spot = spotRepository.findById(spotId).orElseThrow(NotFoundException::new);
        Location newLocation = spotIn.getLocation();
        if (newLocation != null) {
            BeanUtil.copyPropertiesButNull(newLocation, spot.getLocation());
        }
        spotIn.setLocation(null);
        BeanUtil.copyPropertiesButNull(spotIn, spot);
        spot.setUpdatedAt(System.currentTimeMillis());
        spot.setIntro(HtmlUtils.sanitizer(spot.getIntro()));
        spotRepository.save(spot);

        spotTicketRepository.findAllBySpotId(spotId);
        List<SpotTicket> tickets = spotIn.getTickets();
        if (tickets != null && tickets.size() > 0) {
            tickets.forEach(e -> e.setSpotId(spotId));
            spotTicketRepository.saveAll(tickets);
        }

        return spot;
    }

    @Override
    public SpotDetailsOut getSpotDetails(long id, Long userId) {
        Spot spot = spotRepository.findEntityById(id).orElseThrow(NotFoundException::new);
        List<SpotTicket> tickets = spotTicketRepository.findAllBySpotId(id);
        List<SpotPhoto> photos = spotPhotoRepository.findTop3BySpotIdOrderByLikeCountDesc(id);
        SpotDetailsOut out = new SpotDetailsOut(spot, tickets, photos);
        if (userId != null) {
            Map<RelType, LikeCollectRel> rel = likeCollectService.getRelBy(
                    userId,
                    RelObjectType.SPOT,
                    Arrays.asList(RelType.LIKE, RelType.COLLECT),
                    Collections.singletonList(id)).stream().collect(Collectors.toMap(LikeCollectRel::getRelType, e -> e));
            out.setWent(rel.containsKey(RelType.LIKE));
            out.setCollected(rel.containsKey(RelType.COLLECT));
        }
        return out;
    }

    @Override
    public Page<Spot> searchSpot(String keywords, PageParam pageParam) {
        Sort sort = Sort.by(Sort.Direction.DESC, "wentCount");
        return spotRepository.findAllByNameLike(keywords, pageParam.toPageable(sort));
    }

    @Override
    public SpotDetailsOut getSpotWithTicketsById(long id) {
        Spot spot = spotRepository.findById(id).orElseThrow(NotFoundException::new);
        List<SpotTicket> tickets = spotTicketRepository.findAllBySpotId(id);
        return new SpotDetailsOut(spot, tickets, null);
    }

    @Override
    public Spot getSpotById(long id) {
        return spotRepository.findEntityById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Page<Spot> getSpotsByLocationId(int id, PageParam pageParam) {
        Sort sort = Sort.by(Sort.Direction.DESC, "collect".equals(pageParam.getSort()) ? "collectCount" : "wentCount");
        return spotRepository.findAllByLocationLocationIdIn(
                districtService.getFlattingChildren(id).stream().map(District::getId).collect(Collectors.toList()),
                pageParam.toPageable(sort)
        );
    }

    @Override
    public Page<Spot> getSpotList(PageParam pageParam) {
        Sort sort = Sort.by(Sort.Direction.DESC, "wentCount");
        if ("collect".equals(pageParam.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "collectCount");
        }
        return spotRepository.findAll(pageParam.toPageable(sort));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void collectSpot(long id, long userId, boolean state) {
        if (spotRepository.incrementCountById(id, 0, state ? 1 : -1, 0) != 1) {
            throw new NotFoundException();
        }
        if (!likeCollectService.markRelation(state, new LikeCollectRel(userId, id, RelObjectType.SPOT, RelType.COLLECT))) {
            throw new ForbiddenException(state ? "msg.collected" : "msg.not_collected");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wentSpot(long id, long userId, boolean state) {
        if (spotRepository.incrementCountById(id, state ? 1 : -1, 0, 0) != 1) {
            throw new NotFoundException();
        }
        if (!likeCollectService.markRelation(state, new LikeCollectRel(userId, id, RelObjectType.SPOT, RelType.LIKE))) {
            throw new ForbiddenException(state ? "msg.already_went" : "msg.not_went");
        }
    }

    @Cacheable("spot_destination")
    @Override
    public List<SpotDestination> getSpotDestinations() {
        List<Short> locationIds = spotRepository.getDistinctLocationIds();
        Map<Integer, SpotDestination> destinationMap = new HashMap<>();
        for (Short l : locationIds) {
            List<District> fullDistrict = districtService.getFullDistrict(l);
            if (fullDistrict != null && fullDistrict.size() > 0) {
                District root = fullDistrict.get(0);
                SpotDestination destination = destinationMap.get(root.getId());
                if (destination == null) {
                    destinationMap.put(root.getId(), destination = new SpotDestination(root, new LinkedList<>()));
                }
                District self = fullDistrict.get(fullDistrict.size() - 1);
                destination.getChildren().add(self);
            }
        }
        return new LinkedList<>(destinationMap.values());
    }

    @Override
    public Page<Spot> getRecommendSpots(long userId, PageParam pageParam) {
        // TODO implement it
        return getSpotList(pageParam);
    }

    @Override
    public Page<Spot> getHotSpots(PageParam pageParam) {
        // TODO implement it
        pageParam.setSort("went");
        return getSpotList(pageParam);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteSpot(long spotId, long userId) {
        if (!spotRepository.existsById(spotId)) {
            throw new NotFoundException();
        }
        spotRepository.deleteById(spotId);
    }
}
