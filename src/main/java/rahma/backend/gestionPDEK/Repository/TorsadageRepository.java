package rahma.backend.gestionPDEK.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import rahma.backend.gestionPDEK.DTO.SoudureDTO;
import rahma.backend.gestionPDEK.DTO.TorsadageDTO;
import rahma.backend.gestionPDEK.Entity.PagePDEK;
import rahma.backend.gestionPDEK.Entity.Soudure;
import rahma.backend.gestionPDEK.Entity.Torsadage;
import rahma.backend.gestionPDEK.Entity.User;

@Repository

public interface TorsadageRepository extends JpaRepository<Torsadage, Long> {


	@Query("SELECT MAX(s.numeroCycle) FROM Torsadage s WHERE s.pdekTorsadage.id = :pdekId")
	Integer findLastCycleByPdekTorsadage_Id(@Param("pdekId") Long pdekId);
	
	 List<Torsadage> findByPdekTorsadage_Id(Long pdekId);
	 
	 long countByPagePDEK(PagePDEK pagePDEK);
	 
	 @Query("SELECT s.numeroCycle FROM Torsadage s WHERE s.pagePDEK.id = :pageId ORDER BY s.numeroCycle DESC LIMIT 1")
     Optional<Integer> findLastNumeroCycleByPage(@Param("pageId") Long pageId);

	 
	 Optional<Torsadage> findTopByPagePDEK_IdOrderByNumeroCycleDesc(Long pageId);
     List<Torsadage> findByPdekTorsadage_IdAndPagePDEK_PageNumber(Long pdekId, int pageNumber);

	 List<Torsadage> findByDecision(int decision);
	 List<Torsadage> findByDecisionAndRempliePlanAction(int decision, int rempliePlanAction);
	    /********************* Modifier decision a 1 **********************************/
	    @Modifying
	    @Transactional
	    @Query("UPDATE Torsadage p SET p.decision = 1 WHERE p.id = :id")
	    void validerTorsadage(@Param("id") Long id);
	    
	    @Modifying
	    @Transactional
	    @Query("UPDATE Torsadage p SET p.rempliePlanAction = 0 WHERE p.id = :id")
	    void ajoutPlanActionByChefLigne(@Param("id") Long id);
	    
	    
	    @Query("SELECT p.pagePDEK FROM Torsadage p WHERE p.id = :idTorsadage")
	    PagePDEK findPDEKByPagePDEK(@Param("idTorsadage") Long idTorsadage);
	    
	    @Query("SELECT DISTINCT p.userTorsadage FROM Torsadage p WHERE p.pdekTorsadage.id = :idPdek")
	    List<User> findUsersByPdekId(@Param("idPdek") Long idPdek);
     
}

