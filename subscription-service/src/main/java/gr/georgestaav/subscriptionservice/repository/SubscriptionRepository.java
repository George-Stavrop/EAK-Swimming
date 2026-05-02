package gr.georgestaav.subscriptionservice.repository;

import gr.georgestaav.subscriptionservice.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByMobileNumber(String mobileNumber);

    Optional<Subscription> findBySubscriptionNumber(String subscriptionNumber);

    List<Subscription> findByEndDate(LocalDate endDate);
}
