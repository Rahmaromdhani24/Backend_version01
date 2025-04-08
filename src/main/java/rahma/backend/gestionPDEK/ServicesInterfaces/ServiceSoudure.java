package rahma.backend.gestionPDEK.ServicesInterfaces;

import java.util.List;
import java.util.Map;

import rahma.backend.gestionPDEK.DTO.AjoutSoudureResultDTO;
import rahma.backend.gestionPDEK.DTO.SoudureDTO;
import rahma.backend.gestionPDEK.Entity.*;

public interface ServiceSoudure {

public AjoutSoudureResultDTO ajoutPDEKSoudure (Soudure instanceSoudure ,  int matriculeOperateur , String projet) ; 
public Map<Integer, List<SoudureDTO>> recupererSouduresParPDEKGroupéesParPage(String sectionFil, int segment, Plant plant, String nomProjet);
}
