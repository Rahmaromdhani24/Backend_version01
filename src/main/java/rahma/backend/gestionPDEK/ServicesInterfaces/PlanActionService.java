package rahma.backend.gestionPDEK.ServicesInterfaces;

import rahma.backend.gestionPDEK.DTO.DetailsPlanActionDTO;
import rahma.backend.gestionPDEK.Entity.CategoriePistolet;
import rahma.backend.gestionPDEK.Entity.DetailsPlanAction;
import rahma.backend.gestionPDEK.Entity.TypePistolet;

public interface PlanActionService {
	
	 public DetailsPlanActionDTO ajouterPlanActionOuDetails(Long pdekId, int numeroPage, DetailsPlanAction dto, int userId , 
             int numeroPistolet , TypePistolet typePistolet ,CategoriePistolet categoriePistolet) ;
    /*public void ajouterPlanActionTorsadage() ; 
    public void ajouterPlanActionSertissageNormal() ; 
    public void ajouterPlanActionSertissageIDC() ;*/
	
}
