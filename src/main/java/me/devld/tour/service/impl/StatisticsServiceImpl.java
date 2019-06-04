package me.devld.tour.service.impl;

import me.devld.tour.dto.PageParam;
import me.devld.tour.entity.rel.RelObjectType;
import me.devld.tour.service.StatisticsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    /**
     * 热门统计时间，一个月内
     */
    private static final long HOT_PERIOD = 86400000 * 30L;

    private final JdbcTemplate jdbcTemplate;

    public StatisticsServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Page<Long> getHotSpotId(PageParam pageParam) {
        long now = System.currentTimeMillis();
        // language=SQL
        String sql = "SELECT spot_ids.id FROM (SELECT DISTINCT t.obj_id id, COUNT(t.obj_id) FROM tour_like_collect_rel t WHERE t.obj_type = " +
                RelObjectType.SPOT.ordinal() +
                " AND t.created_at BETWEEN ? AND ? GROUP BY t.obj_id ORDER BY COUNT(t.obj_id) DESC) spot_ids";
        return pageQuery(sql, pageParam.toPageable(), new SingleColumnRowMapper<>(Long.class), now - HOT_PERIOD, now);
    }

    @Override
    public Page<Long> getRecommendSpotId(long userId, PageParam pageParam) {
        // language=SQL
        String sql = "SELECT id \n" +
                "FROM   (SELECT DISTINCT spot_id id \n" +
                "        FROM   tour_travel_notes_spot_rel nsl \n" +
                "        WHERE  EXISTS (SELECT DISTINCT lcr.obj_id \n" +
                "                       FROM   tour_like_collect_rel lcr \n" +
                "                       WHERE  lcr.user_id = ? \n" +
                "                              AND lcr.obj_type = " + RelObjectType.TRAVEL_NOTES.ordinal() + " \n" +
                "                              AND lcr.obj_id = nsl.travel_notes_id) \n" +
                "        UNION \n" +
                "        SELECT DISTINCT spot_id id \n" +
                "        FROM   tour_spot_photo sp \n" +
                "        WHERE  EXISTS (SELECT DISTINCT obj_id \n" +
                "                       FROM   tour_like_collect_rel \n" +
                "                       WHERE  user_id = ? \n" +
                "                              AND obj_type = " + RelObjectType.SPOT_PHOTO.ordinal() + " \n" +
                "                              AND obj_id = sp.id) \n" +
                "        UNION \n" +
                "        SELECT DISTINCT obj_id id \n" +
                "        FROM   tour_like_collect_rel t \n" +
                "        WHERE  obj_type = " + RelObjectType.SPOT.ordinal() + " \n" +
                "               AND EXISTS (SELECT user_id \n" +
                "                           FROM   tour_like_collect_rel lcr \n" +
                "                           WHERE  user_id <> ? \n" +
                "                                  AND obj_type = " + RelObjectType.SPOT.ordinal() + " \n" +
                "                                  AND EXISTS (SELECT DISTINCT obj_id \n" +
                "                                              FROM   tour_like_collect_rel \n" +
                "                                              WHERE  user_id = ? \n" +
                "                                                     AND obj_type = " + RelObjectType.SPOT.ordinal() + " \n" +
                "                                                     AND lcr.obj_id = obj_id) \n" +
                "                                  AND t.user_id = user_id)) r \n" +
                "WHERE  NOT EXISTS (SELECT DISTINCT obj_id \n" +
                "                   FROM   tour_like_collect_rel \n" +
                "                   WHERE  user_id = ? \n" +
                "                          AND obj_type = " + RelObjectType.SPOT.ordinal() + " \n" +
                "                          AND r.id = obj_id)";
        return pageQuery(sql, pageParam.toPageable(), new SingleColumnRowMapper<>(Long.class), userId, userId, userId, userId, userId);
    }

    @Override
    public Page<Long> getHotTravelNotesId(PageParam pageParam) {
        long now = System.currentTimeMillis();
        // language=SQL
        String sql = "SELECT notes_ids.id FROM (SELECT DISTINCT t.obj_id id, COUNT(t.obj_id) FROM tour_like_collect_rel t WHERE t.obj_type = " +
                RelObjectType.TRAVEL_NOTES.ordinal() +
                " AND t.created_at BETWEEN ? AND ? GROUP BY t.obj_id ORDER BY COUNT(t.obj_id) DESC) notes_ids";
        return pageQuery(sql, pageParam.toPageable(), new SingleColumnRowMapper<>(Long.class), now - HOT_PERIOD, now);
    }

    private <T> Page<T> pageQuery(String sql, Pageable pageable, RowMapper<T> rowMapper, Object... args) {
        String tName = "t_000";
        String listSql = "SELECT " + tName + ".* FROM (" + sql + ") " + tName + " LIMIT ?, ?";
        String countSql = "SELECT COUNT(1) FROM (" + sql + ") " + tName;
        Object[] newArgs = Arrays.copyOf(args, args.length + 2);
        newArgs[args.length] = pageable.getOffset();
        newArgs[args.length + 1] = pageable.getPageSize();
        List<T> resultList = jdbcTemplate.query(listSql, newArgs, rowMapper);
        long total = Objects.requireNonNull(jdbcTemplate.queryForObject(countSql, args, Long.class));
        return new PageImpl<>(resultList, pageable, total);
    }

}
