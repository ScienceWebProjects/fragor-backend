package com.filament.measurement.Repository;

import com.filament.measurement.Model.Token;
import com.filament.measurement.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {
    @Query("""
            select t from Token t inner join User u on t.user.id = u.id where u.id = :id 
            and (t.expired = false or t.revoked = false)
            """)
     List<Token> findAllValidTokensByUser(Long id);

     Optional<Token> findByToken(String token);

     User findUserByToken(String token);

}
