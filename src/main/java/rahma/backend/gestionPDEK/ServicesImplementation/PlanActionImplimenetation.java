package rahma.backend.gestionPDEK.ServicesImplementation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rahma.backend.gestionPDEK.DTO.DetailsPlanActionDTO;
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
            int numeroPistolet, TypePistolet typePistolet, 
            CategoriePistolet categoriePistolet) {
		
		PagePDEK pagePDEK = pagePDEKRepository.findByPdekIdAndPageNumber(pdekId, numeroPage)
		.orElseThrow(() -> new RuntimeException("PagePDEK introuvable pour le PDEK ID " + pdekId + " et numéro de page " + numeroPage));
		
		PlanAction planAction = planActionRepository.findByPagePDEK(pagePDEK).orElse(null);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		
		if (planAction == null) {
		planAction = PlanAction.builder()
		.dateCreation(LocalDate.now().format(dateFormatter))
		.heureCreation(LocalTime.now().format(timeFormatter))
		.type_operation(TypesOperation.Montage_Pistolet) 
		.pagePDEK(pagePDEK)
		.utilisateursRemplisseurs(new ArrayList<>())
		.details(new ArrayList<>())
		.build();
		planAction = planActionRepository.save(planAction);
		}
		// modifier etat de pistolet
		
		Optional<Pistolet> optionalPistolet = pistoletRepository
		.findTopByNumeroPistoletAndTypeAndCategorieOrderByDateCreationDescHeureCreationDesc(
		numeroPistolet, typePistolet, categoriePistolet);
		
		optionalPistolet.ifPresent(pistolet -> {
		pistoletRepository.ajoutPlanActionByTechnicien(pistolet.getId());
		});
		
		User user = userRepository.findByMatricule(userId).get();
		
		dto.setPlanAction(planAction);
		dto.setUserPlanAction(user);
		dto.setDateCreation(LocalDate.now().format(dateFormatter));
		dto.setHeureCreation(LocalTime.now().format(timeFormatter));
		dto.setSignature_maintenance(1);
		dto.setSignature_contermetre(0);
		dto.setSignature_qualite(0);
		
		DetailsPlanAction savedDetails = detailsPlanActionRepository.save(dto);
		
		planAction.getDetails().add(savedDetails);
		if (!planAction.getUtilisateursRemplisseurs().contains(user)) {
		planAction.getUtilisateursRemplisseurs().add(user);
		}
		

		
		planActionRepository.save(planAction);
		
		return mapToDTO(savedDetails);
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
	    return dto;
	}

}