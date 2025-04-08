package rahma.backend.gestionPDEK.ServicesInterfaces;

import java.util.List;
import java.util.Map;
import rahma.backend.gestionPDEK.DTO.AjoutTorsadageResultDTO;
import rahma.backend.gestionPDEK.DTO.TorsadageDTO;
import rahma.backend.gestionPDEK.Entity.*;

public interface ServiceTorsadage {

	public AjoutTorsadageResultDTO ajoutPDEK_Torsadage (Torsadage instanceTorsadage ,  int matriculeOperateur , String projet) ; 
	public Map<Integer, List<TorsadageDTO>> recupererTorsadagesParPDEKGroupéesParPage(String specificationMesure, int segment, Plant plant, String nomProjet)  ; 
}
