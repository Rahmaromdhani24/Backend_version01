package rahma.backend.gestionPDEK.ServicesImplementation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rahma.backend.gestionPDEK.Entity.DetailsPlanAction;
import rahma.backend.gestionPDEK.Entity.PagePDEK;
import rahma.backend.gestionPDEK.Entity.PlanAction;
import rahma.backend.gestionPDEK.Entity.User;
import rahma.backend.gestionPDEK.Repository.DetailsPlanActionRepository;
import rahma.backend.gestionPDEK.Repository.PdekPageRepository;
import rahma.backend.gestionPDEK.Repository.PlanActionRepository;
import rahma.backend.gestionPDEK.Repository.UserRepository;
import rahma.backend.gestionPDEK.ServicesInterfaces.PlanActionService;

@Service
public class PlanActionImplimenetation implements PlanActionService {

	@Autowired  PlanActionRepository planActionRepository  ; 
	@Autowired  PdekPageRepository pagePDEKRepository;
	@Autowired  DetailsPlanActionRepository detailsPlanActionRepository;
	@Autowired  UserRepository userRepository;
	
	@Override
	@Transactional
    public PlanAction ajouterPlanActionOuDetails(Long pagePdekId, DetailsPlanAction dto, int userId) {
        // Récupérer la pagePDEK
        PagePDEK pagePDEK = pagePDEKRepository.findById(pagePdekId)
            .orElseThrow(() -> new RuntimeException("PagePDEK introuvable"));

        // Vérifier si un PlanAction existe déjà pour cette page
        PlanAction planAction = planActionRepository.findByPagePDEK(pagePDEK).get();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        // Si non, créer le PlanAction
        if (planAction == null) {
            planAction = PlanAction.builder()
                .dateCreation(LocalDate.now().format(dateFormatter))
                .heureCreation(LocalTime.now().format(timeFormatter))
                .type_operation(null) 
                .description_decision("") 
                .signature_qualite(0)
                .signature_maintenance(0)
                .signature_contermetre(0)
                .pagePDEK(pagePDEK)
                .utilisateursRemplisseurs(new ArrayList<>())
                .details(new ArrayList<>())
                .build();

            planAction = planActionRepository.save(planAction);
        }

        // Récupérer l'utilisateur
        User user = userRepository.findByMatricule(userId).get() ;

        // Ajouter DetailsPlanAction
        dto.setPlanAction(planAction);
        dto.setUserPlanAction(user);
        DetailsPlanAction savedDetails = detailsPlanActionRepository.save(dto);

        // Ajouter aux listes
        planAction.getDetails().add(savedDetails);
        if (!planAction.getUtilisateursRemplisseurs().contains(user)) {
            planAction.getUtilisateursRemplisseurs().add(user);
        }

        // Sauvegarder le PlanAction mis à jour
        return planActionRepository.save(planAction);
    }

}
