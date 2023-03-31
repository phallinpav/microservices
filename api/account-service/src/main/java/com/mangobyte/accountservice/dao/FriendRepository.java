package com.mangobyte.accountservice.dao;

import com.mangobyte.accountservice.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("SELECT CASE WHEN (accountFrom.id=?1) THEN accountTo.id ELSE accountFrom.id END FROM Friend " +
            "WHERE (accountFrom.id = ?1 OR accountTo.id = ?1) AND status = 'ACCEPTED'")
    List<Long> findFriendOfAccountId(Long accountId);
}
