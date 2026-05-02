package gr.georgestaav.accessservice.repository;

import gr.georgestaav.accessservice.entity.AccessCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessCardRepository extends JpaRepository<AccessCard,Long> {

    Optional<AccessCard> findByMobileNumber(String mobileNumber);

    Optional<AccessCard> findByAccessCardNumber(String accessCardNumber);
}
