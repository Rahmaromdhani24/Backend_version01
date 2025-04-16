package rahma.backend.gestionPDEK.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.Entity.PlanAction;

@Repository
public interface PlanActionRepository extends JpaRepository<PlanAction, Long> {

}
