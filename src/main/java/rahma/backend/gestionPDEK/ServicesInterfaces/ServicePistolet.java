package rahma.backend.gestionPDEK.ServicesInterfaces;

import java.util.List;
import rahma.backend.gestionPDEK.DTO.PistoletDTO;

public interface ServicePistolet {

	public List<PistoletDTO> getPistoletsNonValidees() ; 
	public List<PistoletDTO> getPistoletsValidees() ; 
	public void validerPistolet(Long idPistolet, int matriculeUser)  ; 
}
