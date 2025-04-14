package rahma.backend.gestionPDEK.ServicesInterfaces;

import java.util.List;
import rahma.backend.gestionPDEK.DTO.SertissageNormal_DTO;

public interface ServiceSertissageNormal {

public List<SertissageNormal_DTO> getSertissagesNonValidees() ; 
public List<SertissageNormal_DTO> getSertissagesValidees() ; 
}
