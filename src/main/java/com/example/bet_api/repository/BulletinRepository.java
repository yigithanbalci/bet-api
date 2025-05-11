package com.example.bet_api.repository;

import com.example.bet_api.model.Bulletin;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BulletinRepository extends JpaRepository<Bulletin, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT b FROM Bulletin b WHERE b.eventId = :eventId")
    Optional<Bulletin> findByIdWithReadLock(@Param("eventId") Long eventId);
}
