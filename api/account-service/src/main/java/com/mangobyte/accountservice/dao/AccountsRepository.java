package com.mangobyte.accountservice.dao;

import com.mangobyte.accountservice.model.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountsRepository extends JpaRepository<Account, Long> {

    @Query("SELECT new Account(a, CASE WHEN (f.accountFrom.id = ?1 OR f.accountTo.id = ?1) " +
            "AND f.status = 'ACCEPTED' THEN true ELSE false END AS isFriend) " +
            "FROM Account a " +
            "LEFT JOIN Friend AS f ON (a.id = f.accountFrom.id OR a.id = f.accountTo.id) " +
            "AND (f.accountFrom.id = ?1 OR f.accountTo.id = ?1) " +
            "AND f.status = 'ACCEPTED' " +
            "WHERE a.id NOT IN " +
            "(SELECT CASE WHEN b.accountFrom.id = ?1 THEN b.accountTo.id ELSE b.accountFrom.id END AS id " +
            "FROM Block b " +
            "WHERE (b.accountFrom.id = ?1 OR b.accountTo.id = ?1) AND b.isBlocked = true) " +
            "AND a.id != ?1 " +
            "ORDER BY isFriend DESC, a.username ASC")
    Page<Account> getAccounts(long userAccId, Pageable pageable);


    @Query("SELECT new Account(a, CASE WHEN (f.accountFrom.id = ?1 OR f.accountTo.id = ?1) " +
            "AND f.status = 'ACCEPTED' THEN true ELSE false END AS isFriend) " +
            "FROM Account a " +
            "LEFT JOIN Friend AS f ON (a.id = f.accountFrom.id OR a.id = f.accountTo.id) " +
            "AND (f.accountFrom.id = ?1 OR f.accountTo.id = ?1) " +
            "AND f.status = 'ACCEPTED' " +
            "WHERE a.id NOT IN " +
            "(SELECT CASE WHEN b.accountFrom.id = ?1 THEN b.accountTo.id ELSE b.accountFrom.id END AS id " +
            "FROM Block b " +
            "WHERE (b.accountFrom.id = ?1 OR b.accountTo.id = ?1) AND b.isBlocked = true) " +
            "AND a.id != ?1 AND LOWER( username ) LIKE %?2% " +
            "ORDER BY isFriend DESC, a.username ASC")
    Page<Account> getAccountsByUsername(long userAccId, String username, Pageable pageable);

}
