package gr.georgestaav.membershipservice.repository;

import gr.georgestaav.membershipservice.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface MembershipRepository extends JpaRepository<Membership,Long> {
    Optional<Membership> findByMemberId(Long memberId);

    @Transactional
    @Modifying
    void deleteByMemberId(Long memberId);

    Optional<Membership> findByMembershipNumber(Long membershipNumber);
}
