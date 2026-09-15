package com.shortly.Repository;

import com.shortly.DTO.userDTOs.UserProfileWithData;
import com.shortly.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("""
                SELECT new com.shortly.DTO.userDTOs.UserProfileWithData(
                usr.userId, usr.username, usr.email, usr.createdAt, usr.updatedAt,
                COALESCE(COUNT(url.id), 0), COALESCE(SUM(url.visit), 0),
                SUM(CASE WHEN url.isActive = true THEN 1 ELSE 0 END))
                FROM User usr
                LEFT JOIN UrlMap url on url.user.id = usr.id
                where usr.username = :username
                GROUP BY usr.username, usr.userId
    """)
    UserProfileWithData getUserProfileWithData(@Param("username") String username);

    List<User> findByUsernameOrEmail(String username, String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
