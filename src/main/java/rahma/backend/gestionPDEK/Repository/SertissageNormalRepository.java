package rahma.backend.gestionPDEK.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.Entity.PagePDEK;
import rahma.backend.gestionPDEK.Entity.SertissageNormal;

@Repository

public interface SertissageNormalRepository extends JpaRepository<SertissageNormal, Long> {

    // Compter le nombre de SertissageIDC associés à une page donnée
    long countByPagePDEK(PagePDEK pagePDEK);

    // Trouver le dernier SertissageIDC sur une page donnée, trié par numCycle en ordre décroissant
    Optional<SertissageNormal> findTopByPagePDEKOrderByNumCycleDesc(PagePDEK pagePDEK);
    
     List<SertissageNormal> findByPdekSertissageNormal_Id(Long pdekId) ;
		// TODO Auto-generated method stub

     @Query("SELECT s.numCycle FROM SertissageNormal s WHERE s.pagePDEK.id = :pageId ORDER BY s.numCycle DESC LIMIT 1")
     Optional<Integer> findLastNumCycleByPage(@Param("pageId") Long pageId);

     /*******************************************************************************************/
    Optional<SertissageNormal> findTopByPagePDEK_IdOrderByNumCycleDesc(Long pageId);
     
}

