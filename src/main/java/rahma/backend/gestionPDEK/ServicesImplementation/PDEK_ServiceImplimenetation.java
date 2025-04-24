package rahma.backend.gestionPDEK.ServicesImplementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import rahma.backend.gestionPDEK.DTO.ContenuPagePdekDTO;
import rahma.backend.gestionPDEK.DTO.PdekDTO;
import rahma.backend.gestionPDEK.DTO.PdekResultat;
import rahma.backend.gestionPDEK.DTO.PistoletDTO;
import rahma.backend.gestionPDEK.DTO.SertissageIDC_DTO;
import rahma.backend.gestionPDEK.DTO.SertissageNormal_DTO;
import rahma.backend.gestionPDEK.DTO.SoudureDTO;
import rahma.backend.gestionPDEK.DTO.TorsadageDTO;
import rahma.backend.gestionPDEK.Entity.CategoriePistolet;
import rahma.backend.gestionPDEK.Entity.PDEK;
import rahma.backend.gestionPDEK.Entity.PagePDEK;
import rahma.backend.gestionPDEK.Entity.Pistolet;
import rahma.backend.gestionPDEK.Entity.Plant;
import rahma.backend.gestionPDEK.Entity.SertissageIDC;
import rahma.backend.gestionPDEK.Entity.SertissageNormal;
import rahma.backend.gestionPDEK.Entity.Soudure;
import rahma.backend.gestionPDEK.Entity.Torsadage;
import rahma.backend.gestionPDEK.Entity.TypePistolet;
import rahma.backend.gestionPDEK.Repository.PdekRepository;
import rahma.backend.gestionPDEK.Repository.ProjetRepository;
import rahma.backend.gestionPDEK.ServicesInterfaces.PDEKService;

@Service
public class PDEK_ServiceImplimenetation implements PDEKService {

	
	@Autowired PdekRepository pdekRepository ;
	@Autowired ProjetRepository projetRepository  ;
	@Autowired private JdbcTemplate jdbcTemplate;


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

	@Override
	public List<ContenuPagePdekDTO> getContenuParPage(Long pdekId) {
	    Optional<PDEK> pdekOptional = pdekRepository.findById(pdekId);

	    if (pdekOptional.isEmpty()) {
	        throw new RuntimeException("PDEK non trouvé avec ID: " + pdekId);
	    }

	    PDEK pdek = pdekOptional.get();
	    List<ContenuPagePdekDTO> result = new ArrayList<>();

	    if (pdek.getPages() != null) {
	        for (PagePDEK page : pdek.getPages()) {
	            int numeroPage = page.getPageNumber();
	            List<Object> contenu = new ArrayList<>();

	            if (pdek.getPdekSoudures() != null) {
	                pdek.getPdekSoudures().stream()
	                    .filter(s -> s.getPagePDEK() != null && s.getPagePDEK().getPageNumber() == numeroPage)
	                    .forEach(contenu::add);
	            }

	            if (pdek.getPdekTorsadages() != null) {
	                pdek.getPdekTorsadages().stream()
	                    .filter(t -> t.getPagePDEK() != null && t.getPagePDEK().getPageNumber() == numeroPage)
	                    .forEach(contenu::add);
	            }


	                    // 🎯 Filtrage des pistolets liés à cette page
	                    if (pdek.getPdekPistoles() != null) {
	                        pdek.getPdekPistoles().stream()
	                            .filter(p -> p.getPagePDEK() != null && p.getPagePDEK().getPageNumber() == numeroPage)
	                            .map(p -> new PistoletDTO(
	                                p.getId(),
	                                p.getPdekPistolet().getId() ,
	                                numeroPage,
	                                p.getSegment(),
	                                p.getDateCreation(),
	                                p.getHeureCreation(),
	                                p.getType(),
	                                p.getNumeroPistolet(),
	                                p.getLimiteInterventionMax(),
	                                p.getLimiteInterventionMin(),
	                                "R" ,
	                                p.getCoupePropre(),
	                                p.getUserPistolet().getMatricule() ,
	                                p.getEch1(),
	                                p.getEch2(),
	                                p.getEch3(),
	                                p.getEch4(),
	                                p.getEch5(),
	                                p.getMoyenne(),
	                                p.getEtendu(),
	                                p.getCategorie(),
	                                p.getNumeroCycle() ,
	                                p.getNbrCollierTester(),
	                                p.getAxeSerrage(),
	                                p.getSemaine(),
	                                p.getDecision(),
	                                p.getUserPistolet().getMatricule() ,
	                                p.getRempliePlanAction()
	                            ))
	                            .forEach(contenu::add);
	                    }


	            if (pdek.getPdekSertissageIDC() != null) {
	                pdek.getPdekSertissageIDC().stream()
	                    .filter(i -> i.getPagePDEK() != null && i.getPagePDEK().getPageNumber() == numeroPage)
	                    .forEach(contenu::add);
	            }

	            if (pdek.getPdekSertissageNormal() != null) {
	                pdek.getPdekSertissageNormal().stream()
	                    .filter(n -> n.getPagePDEK() != null && n.getPagePDEK().getPageNumber() == numeroPage)
	                    .forEach(contenu::add);
	            }

	            if (!contenu.isEmpty()) {
	                result.add(new ContenuPagePdekDTO(numeroPage, contenu));
	            }
	        }
	    }

	    return result;
	}
	public Object getPdekDTOById(Long id) {
	    PDEK pdek = pdekRepository.findById(id).get() ; 

	    switch (pdek.getTypeOperation()) {
	        case Montage_Pistolet:
	            return buildPdekResultat(pdek); // appelle la méthode de mapping personnalisée
	        case Soudure:
	            return buildSoudureDTO(pdek);
	        case Torsadage:
	            return buildTorsadageDTO(pdek);
	        case Sertissage_IDC:
	        	return buildSertissageIDCDTO(pdek);
	        case Sertissage_Normal:
	            return buildSertissageNormalDTO(pdek);
	        default:
	            throw new IllegalArgumentException("Unknown typeOperation: " + pdek.getTypeOperation());
	    }
	}
/******************************************* Build instances **************************/
	public PdekResultat buildPdekResultat(PDEK pdek) {
	    return new PdekResultat(
	        pdek.getId(),
	        pdek.getNumeroPistolet(),
	        pdek.getTypeOperation(), // Enum TypesOperation
	        pdek.getTypePistolet(),  // Enum TypePistolet
	        pdek.getCategoriePistolet(), // Enum CategoriePistolet
	        pdek.getPlant(),         // Enum Plant
	        pdek.getSegment(),
	        0,
	        0
	    );
	}
	public SoudureDTO buildSoudureDTO(PDEK pdek) {
	    SoudureDTO dto = new SoudureDTO();
	    dto.setId(pdek.getId());
	    dto.setCode(null); // suppose que getCode() existe
	    dto.setSectionFil(pdek.getSectionFil()); // suppose que getSectionFil() existe
	    dto.setDate(pdek.getDateCreation()); // ou autre champ selon ton modèle
	    dto.setNumeroCycle(0); // suppose que getNumeroCycle() existe
	    dto.setUserSoudure(0); // ou getUserSoudure() si tu as un champ dédié
	    dto.setMoyenne(0);
	    dto.setEtendu(0);

	    return dto;
	}

	public TorsadageDTO buildTorsadageDTO(PDEK pdek) {
	    TorsadageDTO dto = new TorsadageDTO();
	    dto.setId(pdek.getId());
	    dto.setCode(null); // suppose que getCode() existe
	    dto.setSpecificationMesure(null); // suppose que ce champ existe
	    dto.setDate(pdek.getDateCreation()); // ou un autre champ date
	    dto.setNumeroCycle(0); // champ existant ?
	    dto.setUserTorsadage(0); // ou pdek.getUserTorsadage()
	    dto.setMoyenne(0);
	    dto.setEtendu(0);

	    return dto;
	}
	public SertissageIDC_DTO buildSertissageIDCDTO(PDEK pdek) {
	    SertissageIDC_DTO dto = new SertissageIDC_DTO();
	    dto.setId(pdek.getId());
	    dto.setCode(null); // Assure-toi que getCode() existe
	    dto.setSectionFil(pdek.getSectionFil()); // idem
	    dto.setDate(pdek.getDateCreation()); // ou autre champ si nécessaire
	    dto.setNumCycle(0); // ou pdek.getNumCycle() selon le nom réel
	    dto.setUserSertissageIDC(0); // ou getUserSertissageIDC()

	    return dto;
	}
	public SertissageNormal_DTO buildSertissageNormalDTO(PDEK pdek) {
	    SertissageNormal_DTO dto = new SertissageNormal_DTO();
	    dto.setId(pdek.getId());
	    dto.setCode(null); // Assurez-vous que ce champ existe
	    dto.setSectionFil(pdek.getSectionFil()); // idem
	    dto.setNumOutil(pdek.getNumeroOutils()); // à adapter si le nom réel est différent
	    dto.setNumContact(pdek.getNumeroContacts()); // idem
	    dto.setDate(pdek.getDateCreation()); // ou une autre méthode pour la date
	    dto.setNumCycle(0); // idem
	    dto.setUserSertissageNormal(0); // ou getUserSertissageNormal()

	    dto.setHauteurSertissageEch1(0);
	    dto.setHauteurSertissageEch2(0);
	    dto.setHauteurSertissageEch3(0);
	    dto.setHauteurSertissageEchFin(0);

	    return dto;
	}

}
