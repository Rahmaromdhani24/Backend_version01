package rahma.backend.gestionPDEK.Controllers;

import rahma.backend.gestionPDEK.ServicesImplementation.PistoletServiceImplimenetation;
import rahma.backend.gestionPDEK.ServicesImplementation.PlanActionImplimenetation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rahma.backend.gestionPDEK.DTO.AjoutPistoletResultDTO;
import rahma.backend.gestionPDEK.DTO.PistoletDTO;
import rahma.backend.gestionPDEK.Entity.CategoriePistolet;
import rahma.backend.gestionPDEK.Entity.DetailsPlanAction;
import rahma.backend.gestionPDEK.Entity.Pistolet;
import rahma.backend.gestionPDEK.Entity.PlanAction;
import rahma.backend.gestionPDEK.Entity.Plant;
import rahma.backend.gestionPDEK.Entity.TypePistolet;
import rahma.backend.gestionPDEK.Repository.PistoletRepository;
import rahma.backend.gestionPDEK.Repository.PlanActionRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/planAction")
@RequiredArgsConstructor
public class PlanActionController {

	 @Autowired  private  PlanActionImplimenetation planActionService ;
	 @Autowired  private  PlanActionRepository planActionRepository ;
	 
	 @PostMapping("/addPlanAtcion/{pagePdekId}/ajouter/{userId}")
	 public ResponseEntity<PlanAction> ajouterOuMettreAJour(
	     @PathVariable Long pagePdekId,
	     @PathVariable Integer userId,
	     @RequestBody DetailsPlanAction dto
	 ) {
	     PlanAction pa = planActionService.ajouterPlanActionOuDetails(pagePdekId, dto, userId);
	     return ResponseEntity.ok(pa);
	 }



}
