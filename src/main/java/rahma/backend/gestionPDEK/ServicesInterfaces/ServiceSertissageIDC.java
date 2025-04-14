package rahma.backend.gestionPDEK.ServicesInterfaces;

import java.util.List;
import java.util.Map;

import rahma.backend.gestionPDEK.DTO.AjoutSoudureResultDTO;
import rahma.backend.gestionPDEK.DTO.SertissageIDC_DTO;
import rahma.backend.gestionPDEK.DTO.SoudureDTO;
import rahma.backend.gestionPDEK.Entity.*;

public interface ServiceSertissageIDC {

public List<SertissageIDC_DTO> getSertissagesIDCNonValidees() ; 
public List<SertissageIDC_DTO> getSertissagesIDCValidees() ; 
}
