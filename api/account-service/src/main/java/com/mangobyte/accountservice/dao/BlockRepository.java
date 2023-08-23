package com.mangobyte.accountservice.dao;

import com.mangobyte.accountservice.model.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    @Query("SELECT b FROM Block b WHERE isBlocked = true AND ((accountFrom.id = ?1 AND accountTo.id = ?2) OR (accountTo.id = ?1 AND accountFrom.id = ?2))")
    Optional<Block> findIsBlocked(Long fromAccId, Long toAccId);

    Block save(Block block);
}
