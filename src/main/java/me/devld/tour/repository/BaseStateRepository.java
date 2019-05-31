package me.devld.tour.repository;

import me.devld.tour.entity.BaseStateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseStateRepository<T extends BaseStateEntity> extends CrudRepository<T, Long>, PagingAndSortingRepository<T, Long> {

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.state <> " + BaseStateEntity.STATE_DELETED)
    List<T> findAll(Sort sort);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e")
    List<T> findAllEntity(Sort sort);

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.state <> " + BaseStateEntity.STATE_DELETED)
    Page<T> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e")
    Page<T> findAllEntity(Pageable pageable);

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.id = ?1 AND e.state <> " + BaseStateEntity.STATE_DELETED)
    Optional<T> findById(Long id);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.id = ?1")
    Optional<T> findEntityById(Long id);

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT CASE WHEN (COUNT(e.id) > 0) THEN true ELSE false END FROM #{#entityName} e " +
            "WHERE e.id = ?1 AND e.state <> " + BaseStateEntity.STATE_DELETED)
    boolean existsById(Long id);

    @Transactional(readOnly = true)
    @Query("SELECT CASE WHEN (COUNT(e.id) > 0) THEN true ELSE false END FROM #{#entityName} e " +
            "WHERE e.id = ?1")
    boolean existsEntityById(Long id);

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.state <> " + BaseStateEntity.STATE_DELETED)
    List<T> findAll();

    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e")
    List<T> findAllEntity();

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.id IN ?1 AND e.state <> " + BaseStateEntity.STATE_DELETED)
    List<T> findAllById(Iterable<Long> longs);

    @Transactional(readOnly = true)
    @Query("SELECT e FROM #{#entityName} e WHERE e.id IN ?1")
    List<T> findAllEntityById(Iterable<Long> longs);

    @Override
    @Transactional(readOnly = true)
    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.state <> " + BaseStateEntity.STATE_DELETED)
    long count();

    @Transactional(readOnly = true)
    @Query("SELECT COUNT(e) FROM #{#entityName} e")
    long countEntity();

    @Override
    @Transactional
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.state = " + BaseStateEntity.STATE_DELETED +
            " WHERE e.id = ?1 AND e.state <> " + BaseStateEntity.STATE_DELETED)
    void deleteById(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id = ?1")
    void deleteEntityById(Long id);

    @Override
    @Transactional
    @Modifying
    default void delete(T entity) {
        deleteById(entity.getId());
    }

    @Transactional
    @Modifying
    default void deleteEntity(T entity) {
        deleteEntityById(entity.getId());
    }

    @Override
    @Transactional
    @Modifying
    default void deleteAll(Iterable<? extends T> entities) {
        List<Long> ids = new LinkedList<>();
        for (T o : entities) {
            ids.add(o.getId());
        }
        deleteAllByIds(ids);
    }

    @Transactional
    @Modifying
    default void deleteAllEntity(Iterable<? extends T> entities) {
        List<Long> ids = new LinkedList<>();
        for (T o : entities) {
            ids.add(o.getId());
        }
        deleteAllEntityByIds(ids);
    }

    @Transactional
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.state = " + BaseStateEntity.STATE_DELETED + " WHERE e.id IN ?1")
    void deleteAllByIds(Iterable<Long> ids);

    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.id IN ?1")
    void deleteAllEntityByIds(Iterable<Long> ids);

    @Override
    @Transactional
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.state = " + BaseStateEntity.STATE_DELETED)
    void deleteAll();

    @Transactional
    @Modifying
    @Query("DELETE FROM #{#entityName}")
    void deleteAllEntity();
}
