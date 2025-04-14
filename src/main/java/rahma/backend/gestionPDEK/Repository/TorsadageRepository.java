package rahma.backend.gestionPDEK.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rahma.backend.gestionPDEK.DTO.TorsadageDTO;
import rahma.backend.gestionPDEK.Entity.PagePDEK;
import rahma.backend.gestionPDEK.Entity.Soudure;
import rahma.backend.gestionPDEK.Entity.Torsadage;

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

	 List<TorsadageDTO> findByDecision(int i);
     
}

