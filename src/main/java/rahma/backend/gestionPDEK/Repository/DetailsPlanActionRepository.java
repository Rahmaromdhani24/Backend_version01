package rahma.backend.gestionPDEK.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.Entity.DetailsPlanAction;

@Repository
public interface DetailsPlanActionRepository extends JpaRepository<DetailsPlanAction, Long> {

	List<DetailsPlanAction> findByPlanActionId(Long id);

}
