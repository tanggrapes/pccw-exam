package com.marktoledo.pccwexam.repositories;

import com.marktoledo.pccwexam.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

//   native query to ignore where clause deleted=false
    @Query(value= "SELECT * FROM pccw_exam_user u  WHERE u.email=?1", nativeQuery=true)
    User getUserByEmail(String email);

}
