package rahma.backend.gestionPDEK.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rahma.backend.gestionPDEK.Entity.PagePDEK;
import rahma.backend.gestionPDEK.Entity.Pistolet;
import rahma.backend.gestionPDEK.Entity.User;


@Repository
public interface PistoletRepository extends JpaRepository<Pistolet, Long> {

	long countByPagePDEK(PagePDEK pagePDEK);

	Optional<User> findMaxNumeroCycleByPagePDEK(PagePDEK lastPage);
	
	 @Query("SELECT s.numeroCycle FROM Pistolet s WHERE s.pagePDEK.id = :pageId ORDER BY s.numeroCycle DESC LIMIT 1")
     Optional<Integer> findLastNumCycleByPage(@Param("pageId") Long pageId);

}