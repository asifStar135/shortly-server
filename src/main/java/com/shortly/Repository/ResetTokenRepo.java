package com.shortly.Repository;

import com.shortly.Models.ResetToken;
import com.shortly.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResetTokenRepo extends JpaRepository<ResetToken, Long> {

    List<ResetToken> findByUserAndUsed(User user, boolean b);
}
