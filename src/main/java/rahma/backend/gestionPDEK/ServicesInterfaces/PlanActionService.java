package rahma.backend.gestionPDEK.ServicesInterfaces;

import rahma.backend.gestionPDEK.Entity.DetailsPlanAction;
import rahma.backend.gestionPDEK.Entity.PagePDEK;
import rahma.backend.gestionPDEK.Entity.PlanAction;

public interface PlanActionService {
	
    public PlanAction ajouterPlanActionOuDetails(Long pagePdekId, DetailsPlanAction dto, int userId) ;
    /*public void ajouterPlanActionTorsadage() ; 
    public void ajouterPlanActionSertissageNormal() ; 
    public void ajouterPlanActionSertissageIDC() ;*/
	
}
