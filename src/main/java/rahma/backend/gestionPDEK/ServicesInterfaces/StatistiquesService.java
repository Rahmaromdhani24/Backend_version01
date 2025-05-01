package rahma.backend.gestionPDEK.ServicesInterfaces;

import java.util.List;
import java.util.Map;

import rahma.backend.gestionPDEK.DTO.OperateurErreurDTO;

public interface StatistiquesService {

	public long nombreTotalOperateurs() ; 
	public long nombreHommesOperateurs()  ;
	public long nombreFemmesOperateurs()  ; 
	public double calculerPourcentageAugmentationOperateurs()  ; 
	
	/******************************** Erreurs pour process **********************/
	public long nombreErreursSertissageNormalCetteSemaine() ; 
	public long nombreErreursSertissageIDCCetteSemaine() ; 
	public long nombreErreursSoudureCetteSemaine() ; 
	public long nombreErreursTorsadgeCetteSemaine() ; 
	public long nombreErreursTotalCetteSemaineSaufPistolet() ; 
	public double calculerPourcentageSemainePrecdant()  ; 
	
	
	/****************** pdek  *******************/
	public Map<String, Long> getNombrePdekParTypeOperation() ; 
	public Map<String, Long> getNombrePlanActionParTypeOperation() ; 

	
	/******************** top 5 operateurs ***********************/
	public List<OperateurErreurDTO> getTop5OperateursWithErrors() ;

}
