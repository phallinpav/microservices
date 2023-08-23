package com.mangobyte.accountservice.dao;

import com.mangobyte.accountservice.model.entity.Account;
import com.mangobyte.accountservice.model.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("SELECT new com.mangobyte.accountservice.model.entity.Account(CASE WHEN (accountFrom.id=?1) THEN f.accountTo ELSE f.accountFrom END) " +
            "FROM Friend f " +
            "WHERE (accountFrom.id = ?1 OR accountTo.id = ?1) AND status = 'ACCEPTED'")
    List<Account> findFriendOfAccountId(Long accountId);

    @Query("SELECT f FROM Friend f WHERE accountFrom.id = ?1 AND accountTo.id = ?2 AND status = 'REQUESTING'")
    Optional<Friend> findPendingRequest(Long fromOtherAccId, Long toUserAccId);

    @Query("SELECT f FROM Friend f WHERE (status = 'REQUESTING' OR status = 'ACCEPTED') " +
            "AND ((accountFrom.id = ?1 AND accountTo.id = ?2) OR (accountTo.id = ?1 AND accountFrom.id = ?2))")
    Optional<Friend> findAcceptedOrRequesting(Long userAccId, Long otherUserAccId);

    Friend save(Friend friend);

    @Query("SELECT f FROM Friend f WHERE status = 'ACCEPTED' " +
            "AND ((accountFrom.id = ?1 AND accountTo.id = ?2) OR (accountTo.id = ?1 AND accountFrom.id = ?2))")
    Optional<Friend> findAccepted(Long otherAccId, Long userAccId);

    @Query("SELECT f FROM Friend f " +
            "WHERE accountFrom.id = ?1 AND status = 'REQUESTING'" +
            "ORDER BY requestedAt DESC")
    List<Friend> findRequestingFrom(Long userAccId);

    @Query("SELECT f FROM Friend f " +
            "WHERE accountTo.id = ?1 AND status = 'REQUESTING'" +
            "ORDER BY requestedAt DESC")
    List<Friend> findRequestingTo(Long userAccId);

}
