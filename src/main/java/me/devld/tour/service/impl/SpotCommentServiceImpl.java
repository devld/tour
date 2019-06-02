package me.devld.tour.service.impl;

import me.devld.tour.dto.PageParam;
import me.devld.tour.dto.spot.SpotCommentIn;
import me.devld.tour.dto.spot.SpotCommentOut;
import me.devld.tour.dto.user.UserProfile;
import me.devld.tour.entity.SpotComment;
import me.devld.tour.entity.TourUser;
import me.devld.tour.entity.rel.LikeCollectRel;
import me.devld.tour.entity.rel.RelObjectType;
import me.devld.tour.entity.rel.RelType;
import me.devld.tour.exception.ForbiddenException;
import me.devld.tour.exception.NotFoundException;
import me.devld.tour.repository.SpotCommentRepository;
import me.devld.tour.repository.SpotRepository;
import me.devld.tour.service.LikeCollectService;
import me.devld.tour.service.SpotCommentService;
import me.devld.tour.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpotCommentServiceImpl implements SpotCommentService {

    private final SpotCommentRepository spotCommentRepository;
    private final UserService userService;
    private final LikeCollectService likeCollectService;
    private final SpotRepository spotRepository;

    public SpotCommentServiceImpl(SpotCommentRepository spotCommentRepository,
                                  UserService userService,
                                  LikeCollectService likeCollectService,
                                  SpotRepository spotRepository) {
        this.spotCommentRepository = spotCommentRepository;
        this.userService = userService;
        this.likeCollectService = likeCollectService;
        this.spotRepository = spotRepository;
    }

    @Override
    public Page<SpotCommentOut> getSpotComments(long spotId, PageParam pageParam, Long userId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt", "likeCount");
        if ("like".equals(pageParam.getSort())) {
            sort = Sort.by(Sort.Direction.DESC, "likeCount", "updatedAt");
        }
        Page<SpotComment> comments = spotCommentRepository.findAllBySpotId(spotId, pageParam.toPageable(sort));
        Map<Long, UserProfile> userProfileMap = userService.getUserInfos(comments.stream().map(SpotComment::getAuthorId).collect(Collectors.toList()));
        Page<SpotCommentOut> commentOuts = comments
                .map(e -> new SpotCommentOut(e, userProfileMap.get(e.getAuthorId())));

        if (userId != null) {
            Map<Long, LikeCollectRel> likeRel = likeCollectService.getRelBy(
                    userId, RelObjectType.SPOT_COMMENT, Collections.singletonList(RelType.LIKE),
                    commentOuts.map(e -> e.getComment().getId()).getContent()).stream().collect(Collectors.toMap(LikeCollectRel::getObjId, e -> e));
            for (SpotCommentOut o : commentOuts) {
                o.setLiked(likeRel.containsKey(o.getComment().getId()));
            }
        }

        return commentOuts;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SpotComment commentSpot(SpotCommentIn comment, long spotId, long authorId) {
        if (spotRepository.incrementCountById(spotId, 0, 0, 1) != 1) {
            throw new NotFoundException();
        }
        SpotComment spotComment = new SpotComment();
        spotComment.setContent(comment.getContent());
        spotComment.setAuthorId(authorId);
        spotComment.setSpotId(spotId);
        return spotCommentRepository.save(spotComment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void likeSpotComment(long commentId, long userId, boolean state) {
        if (spotCommentRepository.incrementCountById(commentId, state ? 1 : -1) != 1) {
            throw new NotFoundException();
        }
        if (!likeCollectService.markRelation(state, new LikeCollectRel(userId, commentId, RelObjectType.SPOT_COMMENT, RelType.LIKE))) {
            throw new ForbiddenException(state ? "msg.already_liked" : "msg.not_liked");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteComment(long commentId, long userId) {
        SpotComment comment = spotCommentRepository.findById(commentId).orElseThrow(NotFoundException::new);
        if (comment.getAuthorId() != userId) {
            if (userService.findUserById(userId).getUserType() != TourUser.UserType.ADMIN) {
                throw new ForbiddenException();
            }
        }
        spotCommentRepository.deleteById(commentId);
    }
}
