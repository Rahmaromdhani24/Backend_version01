package rahma.backend.gestionPDEK.ServicesImplementation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rahma.backend.gestionPDEK.DTO.DetailsPlanActionDTO;
import rahma.backend.gestionPDEK.DTO.PlanActionDTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;
import rahma.backend.gestionPDEK.Entity.CategoriePistolet;
import rahma.backend.gestionPDEK.Entity.DetailsPlanAction;
import rahma.backend.gestionPDEK.Entity.PagePDEK;
import rahma.backend.gestionPDEK.Entity.Pistolet;
import rahma.backend.gestionPDEK.Entity.PlanAction;
import rahma.backend.gestionPDEK.Entity.TypePistolet;
import rahma.backend.gestionPDEK.Entity.TypesOperation;
import rahma.backend.gestionPDEK.Entity.User;
import rahma.backend.gestionPDEK.Repository.DetailsPlanActionRepository;
import rahma.backend.gestionPDEK.Repository.PdekPageRepository;
import rahma.backend.gestionPDEK.Repository.PistoletRepository;
import rahma.backend.gestionPDEK.Repository.PlanActionRepository;
import rahma.backend.gestionPDEK.Repository.UserRepository;
import rahma.backend.gestionPDEK.ServicesInterfaces.PlanActionService;

@Service
public class PlanActionImplimenetation implements PlanActionService {

	@Autowired  PlanActionRepository planActionRepository  ; 
	@Autowired  PdekPageRepository pagePDEKRepository;
	@Autowired  DetailsPlanActionRepository detailsPlanActionRepository;
	@Autowired  UserRepository userRepository;
	@Autowired  private PistoletRepository pistoletRepository;

	@Override
	@Transactional
	public DetailsPlanActionDTO ajouterPlanActionOuDetails(Long pdekId, int numeroPage, DetailsPlanAction dto, int userId, 
	        int numeroPistolet, TypePistolet typePistolet, CategoriePistolet categoriePistolet) {
	    
	    // Récupérer la page PDEK
	    PagePDEK pagePDEK = pagePDEKRepository.findByPdekIdAndPageNumber(pdekId, numeroPage)
	        .orElseThrow(() -> new RuntimeException("PagePDEK introuvable pour le PDEK ID " + pdekId + " et numéro de page " + numeroPage));
	    
	    // Chercher le plan d'action associé à cette page
	    PlanAction planAction = planActionRepository.findByPagePDEK(pagePDEK).orElse(null);
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	    
	    // Si aucun plan d'action n'existe pour cette page, on en crée un nouveau
	    if (planAction == null) {
	        planAction = PlanAction.builder()
	            .dateCreation(LocalDate.now().format(dateFormatter))
	            .heureCreation(LocalTime.now().format(timeFormatter))
	            .type_operation(TypesOperation.Montage_Pistolet) 
	            .pagePDEK(pagePDEK)
	            .userPlanAction(null)  // L'utilisateur sera défini plus tard
	            .details(new ArrayList<>())  // Liste de détails vide
	            .build();
	        
	        // Sauvegarder le nouveau plan d'action
	        planAction = planActionRepository.save(planAction);
	    }

	    // Récupérer l'utilisateur par matricule
	    User user = userRepository.findByMatricule(userId).get();
	    
	    // Associer l'utilisateur au plan d'action
	    planAction.setUserPlanAction(user);
	    
	    // Sauvegarder les changements dans le plan d'action (y compris la relation avec l'utilisateur)
	    planActionRepository.save(planAction);
	    
	    // Ajouter les informations dans le DTO
	    dto.setPlanAction(planAction);
	    dto.setUserPlanAction(user);
	    dto.setDateCreation(LocalDate.now().format(dateFormatter));
	    dto.setHeureCreation(LocalTime.now().format(timeFormatter));
	    dto.setSignature_maintenance(1);  // Exemple de valeur pour la signature
	    dto.setSignature_contermetre(0);
	    dto.setSignature_qualite(0);
	    
	    // Sauvegarder les détails du plan d'action
	    DetailsPlanAction savedDetails = detailsPlanActionRepository.save(dto);
	    
	    // Ajouter les détails au plan d'action
	    planAction.getDetails().add(savedDetails);
	    
	    // Sauvegarder les détails dans le plan d'action
	    planActionRepository.save(planAction);
	    
	       // modifier etat de pistolet
		
	 		Optional<Pistolet> optionalPistolet = pistoletRepository
	 		.findTopByNumeroPistoletAndTypeAndCategorieOrderByDateCreationDescHeureCreationDesc(
	 		numeroPistolet, typePistolet, categoriePistolet);
	 		
	 		optionalPistolet.ifPresent(pistolet -> {
	 		pistoletRepository.ajoutPlanActionByTechnicien(pistolet.getId());
	 		});
	 		
	    // Retourner le DTO des détails
	    return mapToDTO(savedDetails);
	}

	@Override
	public PlanActionDTO testerPdekPistoletPossedePlanAction(long pdekId) {
	    // 1. Chercher la page PDEK par pdekId
	    Optional<PagePDEK> optionalPagePDEK = pagePDEKRepository.findByPdekId(pdekId);

	    if (optionalPagePDEK.isPresent()) {
	        PagePDEK pagePDEK = optionalPagePDEK.get();

	        // 2. Vérifier si un plan d'action est associé à cette page
	        Optional<PlanAction> optionalPlanAction = planActionRepository.findByPagePDEK(pagePDEK);

	        if (optionalPlanAction.isPresent()) {
	            PlanAction planAction = optionalPlanAction.get();

	            // 3. Mapper vers le DTO
	            PlanActionDTO dto = new PlanActionDTO();
	            dto.setId(planAction.getId());
	            dto.setDateCreation(planAction.getDateCreation());
	            dto.setHeureCreation(planAction.getHeureCreation());

	            return dto;
	        }
	    }

	    // Aucun plan d'action trouvé pour cette page
	    return null;
	}
	@Override
	public List<DetailsPlanActionDTO> getDetailsByPlanActionId(Long id) {
	    List<DetailsPlanAction> entities = detailsPlanActionRepository.findByPlanActionId(id);
	    return entities.stream().map(this::mapToDTO).collect(Collectors.toList());
	}
	private DetailsPlanActionDTO mapToDTO(DetailsPlanAction details) {
	    DetailsPlanActionDTO dto = new DetailsPlanActionDTO();
	    dto.setId(details.getId());
	    dto.setDateCreation(details.getDateCreation());
	    dto.setHeureCreation(details.getHeureCreation());
	    dto.setDescription_probleme(details.getDescription_probleme());
	    dto.setMatricule_operateur(details.getMatricule_operateur());
	    dto.setMatricule_chef_ligne(details.getMatricule_chef_ligne());
	    dto.setDescription_decision(details.getDescription_decision());
	    dto.setSignature_qualite(details.getSignature_qualite());
	    dto.setSignature_maintenance(details.getSignature_maintenance());
	    dto.setSignature_contermetre(details.getSignature_contermetre());
	    dto.setIdPlanAction(details.getPlanAction().getId());
	    dto.setUserPlanAction(details.getUserPlanAction().getMatricule());
	    dto.setResponsable(details.getResponsable());
	    dto.setDelais(details.getDelais());
	    return dto;
	}

	@Override
	public List<PlanActionDTO> getPlansActionByTypeOperation(TypesOperation typeOperation) {
	    List<PlanAction> plans = planActionRepository.getByTypeOperation(typeOperation);

	    return plans.stream().map(plan -> {
	        PlanActionDTO dto = new PlanActionDTO();
	        dto.setId(plan.getId());
	        dto.setDateCreation(plan.getDateCreation());
	        dto.setHeureCreation(plan.getHeureCreation());
	        dto.setType_operation(plan.getType_operation());
	        if (plan.getPagePDEK() != null) {
	            dto.setPagePdekId(plan.getPagePDEK().getId());
	            // Récupérer le pdek.id à partir de la pagePDEK
	            if (plan.getPagePDEK().getPdek() != null) {
	                dto.setPdekId(plan.getPagePDEK().getPdek().getId());
	            }
	        }
	        dto.setMatriculeUser(plan.getUserPlanAction().getMatricule()) ;
	        return dto;
	    }).collect(Collectors.toList());
	}


	@Override
	   public List<UserDTO> getUsersByPlanActionId(Long planActionId) {
        // Récupérer tous les détails associés à ce plan d'action
        List<DetailsPlanAction> details = detailsPlanActionRepository.findByPlanActionId(planActionId);

        // Extraire les utilisateurs et les convertir en DTO (en évitant les doublons avec un Set)
        return details.stream()
                .map(DetailsPlanAction::getUserPlanAction)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                    User::getMatricule,
                    UserDTO::fromEntity,
                    (existing, replacement) -> existing // Garde le premier s’il y a doublon
                ))
                .values()
                .stream()
                .collect(Collectors.toList());

    }

	@Override
	public List<PlanActionDTO> testerPdeksProcessPossedePlanAction(long pdekId) {
		  List<PagePDEK> pages = pagePDEKRepository.findAllByPdekId(pdekId);
		    List<PlanActionDTO> result = new ArrayList<>();

		    for (PagePDEK page : pages) {
		        List<PlanAction> plans = planActionRepository.findAllByPagePDEK(page);

		        for (PlanAction plan : plans) {
		            PlanActionDTO dto = new PlanActionDTO();
		            dto.setId(plan.getId());
		            dto.setDateCreation(plan.getDateCreation());
		            dto.setHeureCreation(plan.getHeureCreation());
		            result.add(dto);
		        }
		    }

		    return result;
		}

}