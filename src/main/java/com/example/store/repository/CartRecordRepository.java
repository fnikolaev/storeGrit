package com.example.store.repository;

import com.example.store.entity.CartRecord;
import com.example.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRecordRepository extends JpaRepository<CartRecord, Long> {
    List<CartRecord> findCartRecordsByUser(User user);
}
