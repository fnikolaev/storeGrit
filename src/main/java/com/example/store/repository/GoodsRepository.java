package com.example.store.repository;

import com.example.store.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface GoodsRepository extends JpaRepository<Goods,Long> {
    Goods findByTitle(String title);

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Goods> findById(Long id);

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ)
    Goods getById(Long aLong);
}
