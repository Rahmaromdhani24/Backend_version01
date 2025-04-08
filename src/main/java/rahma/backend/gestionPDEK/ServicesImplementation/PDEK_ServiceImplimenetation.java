package rahma.backend.gestionPDEK.ServicesImplementation;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rahma.backend.gestionPDEK.DTO.PdekDTO;
import rahma.backend.gestionPDEK.DTO.PistoletDTO;
import rahma.backend.gestionPDEK.Entity.CategoriePistolet;
import rahma.backend.gestionPDEK.Entity.PDEK;
import rahma.backend.gestionPDEK.Entity.Plant;
import rahma.backend.gestionPDEK.Entity.TypePistolet;
import rahma.backend.gestionPDEK.Entity.TypesOperation;
import rahma.backend.gestionPDEK.Repository.PdekRepository;
import rahma.backend.gestionPDEK.Repository.ProjetRepository;
import rahma.backend.gestionPDEK.ServicesInterfaces.PDEKService;

@Service
public class PDEK_ServiceImplimenetation implements PDEKService {

	
	@Autowired PdekRepository pdekRepository ;
	@Autowired ProjetRepository projetRepository  ;



	public boolean verifierExistencePDEK_soudureUltrason(String sectionFil, int segment ,Plant plant , String nomProjet ) {
		  Optional<PDEK> pdekExiste = pdekRepository.findUniquePDEK_SoudureUtrason(sectionFil , segment , plant , nomProjet );
	    return pdekExiste.isPresent();
	}

	public PdekDTO recupererPdekSoudureUltrason(String sectionFil, int segment ,Plant plant , String nomProjet) {
	    return pdekRepository.findUniquePDEK_SoudureUtrason(sectionFil , segment , plant , nomProjet )
	        .map(pdek -> new PdekDTO(
	                pdek.getId(),
	                pdek.getSectionFil(),
	                nomProjet , 
	                pdek.getDateCreation(),
	                pdek.getFrequenceControle()
	        ))
	        .orElse(null); // Ou Optional<PdekDTO> si tu veux gérer l'absence
	}

	
	public boolean verifierExistencePDEK_Torsadage(String sectionFil, int segment ,Plant plant , String nomProjet ) {
		  Optional<PDEK> pdekExiste = pdekRepository.findUniquePDEK_Torsadage(sectionFil , segment , plant , nomProjet );
	    return pdekExiste.isPresent();
	}

	public PdekDTO recupererPdekTorsadag(String sectionFil, int segment ,Plant plant , String nomProjet) {
	    return pdekRepository.findUniquePDEK_Torsadage(sectionFil , segment , plant , nomProjet )
	        .map(pdek -> new PdekDTO(
	                pdek.getId(),
	                pdek.getSectionFil(),
	                nomProjet , 
	                pdek.getDateCreation(),
	                pdek.getFrequenceControle()
	        ))
	        .orElse(null); // Ou Optional<PdekDTO> si tu veux gérer l'absence
	}

	@Override
	public boolean verifierExistencePDEK_Pistolet(TypePistolet typePistolet , CategoriePistolet categoriePistolet , int numeroPistolet ) {
		  Optional<PDEK> pdekExiste = pdekRepository.findByTypePistoletAndCategoriePistoletAndNumeroPistolet(typePistolet   , categoriePistolet ,numeroPistolet );
		    return pdekExiste.isPresent();
	}
	
	@Override
	public PistoletDTO recupererPdek_Pistolet(TypePistolet typePistolet , CategoriePistolet categoriePistolet , int numeroPistolet ) {
	    // Recherche un PDEK selon le type de pistolet
	    Optional<PDEK> pdekOpt = pdekRepository.findByTypePistoletAndCategoriePistoletAndNumeroPistolet(typePistolet   , categoriePistolet ,numeroPistolet );
	    
	    // Si le PDEK existe, on cherche un Pistolet associé au type de Pistolet
	    if (pdekOpt.isPresent()) {
	        PDEK pdek = pdekOpt.get();
	        
	        // Chercher le premier Pistolet qui correspond au type de Pistolet
	        return pdek.getPdekPistoles().stream()
	                .filter(pistolet -> pistolet.getTypePistolet() == typePistolet) // Filtrage par type de pistolet
	                .findFirst() // Récupère le premier Pistolet trouvé
	                .map(pistolet -> new PistoletDTO(
	                        pistolet.getId(),
	                        pistolet.getDateCreation(),
	                        pistolet.getTypePistolet(),
	                        pistolet.getNumeroPistolet(),
	                        pistolet.getLimiteInterventionMax(),
	                        pistolet.getLimiteInterventionMin(),
	                        pistolet.getPdekPistolet() // Associer le PDEK
	                ))
	                .orElse(null); // Si aucun Pistolet n'est trouvé, retourner null
	    }
	    
	    // Si aucun PDEK n'est trouvé, retourner null
	    return null;
	}

}
