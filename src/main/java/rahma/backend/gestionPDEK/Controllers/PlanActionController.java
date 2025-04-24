package rahma.backend.gestionPDEK.Controllers;

import rahma.backend.gestionPDEK.ServicesImplementation.PlanActionImplimenetation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rahma.backend.gestionPDEK.DTO.DetailsPlanActionDTO;
import rahma.backend.gestionPDEK.DTO.PlanActionDTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;
import rahma.backend.gestionPDEK.Entity.CategoriePistolet;
import rahma.backend.gestionPDEK.Entity.DetailsPlanAction;
import rahma.backend.gestionPDEK.Entity.TypePistolet;
import rahma.backend.gestionPDEK.Entity.TypesOperation;
import rahma.backend.gestionPDEK.Repository.PlanActionRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/planAction")
@RequiredArgsConstructor
public class PlanActionController {

	 @Autowired  private  PlanActionImplimenetation planActionService ;
	 @Autowired  private  PlanActionRepository planActionRepository ;
	 
	 @PostMapping("/addPlanAction/{pdekId}/{numeroPage}/{userId}/{numeroPistolet}/{typePistolet}/{categoriePistolet}")
	 public ResponseEntity<?> ajouterOuMettreAJour(
	     @PathVariable Long pdekId,
	     @PathVariable int numeroPage,
	     @PathVariable int userId,
	     @PathVariable int numeroPistolet,
	     @PathVariable String typePistolet,
	     @PathVariable String categoriePistolet,
	     @RequestBody DetailsPlanAction dto
	 ) {
	     try {
	         // Conversion des chaînes vers les enums (en majuscules)
	         TypePistolet typeEnum = TypePistolet.valueOf(typePistolet);
	         CategoriePistolet categorieEnum = CategoriePistolet.valueOf(categoriePistolet);

	         DetailsPlanActionDTO dtoResponse = planActionService.ajouterPlanActionOuDetails(
	                 pdekId,
	                 numeroPage,
	                 dto,
	                 userId,
	                 numeroPistolet,
	                 typeEnum,
	                 categorieEnum
	             );

	         return ResponseEntity.ok(dtoResponse);
	     } catch (IllegalArgumentException e) {
	         String messageErreur = String.format(
	             "Valeur invalide : typePistolet='%s' ou categoriePistolet='%s' ne correspond pas aux valeurs attendues.",
	             typePistolet, categoriePistolet
	         );
	         return ResponseEntity.badRequest().body(messageErreur);
	     }
	 }
	 
	 @GetMapping("/testerPdek/{pdekId}")
	    public ResponseEntity<PlanActionDTO> testerPdekPossedePlanAction(@PathVariable long pdekId) {
	        PlanActionDTO dto = planActionService.testerPdekPistoletPossedePlanAction(pdekId);

	        if (dto != null) {
	            return ResponseEntity.ok(dto);
	        } else {
	            return ResponseEntity.noContent().build(); // 204 No Content si aucun plan trouvé
	        }
	    }
	 
	 @GetMapping("/{id}/details")
	    public ResponseEntity<List<DetailsPlanActionDTO>> getDetails(@PathVariable Long id) {
	        List<DetailsPlanActionDTO> details = planActionService.getDetailsByPlanActionId(id);
	        return ResponseEntity.ok(details);
	    }
	 
	 @GetMapping("/plansActions/{typeOperation}")
	    public List<PlanActionDTO> getPlanActionsByType(@PathVariable TypesOperation typeOperation) {
	        return planActionService.getPlansActionByTypeOperation(typeOperation);
	    }
	 
	 @GetMapping("/users-by-planAction/{id}")
	    public ResponseEntity<List<UserDTO>> getUsersByPlanActionId(@PathVariable Long id) {
	        List<UserDTO> users = planActionService.getUsersByPlanActionId(id);
	        return ResponseEntity.ok(users);
	    }
}
