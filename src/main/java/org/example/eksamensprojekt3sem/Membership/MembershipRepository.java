package org.example.eksamensprojekt3sem.Membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
}
