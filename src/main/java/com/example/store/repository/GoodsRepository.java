package com.example.store.repository;

import com.example.store.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface GoodsRepository extends JpaRepository<Goods,Long> {
    Goods findByTitle(String title);

    @Override
    Goods getById(Long aLong);
}
