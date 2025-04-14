package rahma.backend.gestionPDEK.Controllers;

import rahma.backend.gestionPDEK.ServicesImplementation.PistoletServiceImplimenetation;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rahma.backend.gestionPDEK.DTO.AjoutPistoletResultDTO;
import rahma.backend.gestionPDEK.DTO.PistoletDTO;
import rahma.backend.gestionPDEK.Entity.Pistolet;
import rahma.backend.gestionPDEK.Entity.Plant;
import rahma.backend.gestionPDEK.Repository.PistoletRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/operations/pistolet")
@RequiredArgsConstructor
public class PistoletController {

	 @Autowired  private  PistoletServiceImplimenetation pistoletService;
	 @Autowired  private  PistoletRepository pistoletRepository ;
	 
	 @PostMapping("/ajouterPDEK/{matricule}")
	 public ResponseEntity<String> ajouterPDEK(@PathVariable int matricule, @RequestBody Pistolet pistolet) {
	     try {
	    	 AjoutPistoletResultDTO result = pistoletService.ajouterPistolet(matricule, pistolet);
	    	   String jsonResponse = "{ \"pdekId\": \"" + result.getPdekId() + "\", \"pageNumber\": \"" + result.getNumeroPage() + "\" }";
	            return ResponseEntity.ok(jsonResponse);
	     } catch (Exception e) {
	         return ResponseEntity.badRequest().build(); // Retourne un 400 sans message
	     }
	 }


	 @GetMapping("/dernier-numero-cycle")
	    public ResponseEntity<?> getLastNumeroCycle(
	            @RequestParam String typePistolet,
	            @RequestParam int numPistolet,
	            @RequestParam String categorie,
	            @RequestParam int segment,
	            @RequestParam Plant nomPlant) {
	        int dernierNumeroCycle = pistoletService.getLastNumeroCycle(typePistolet, segment, numPistolet ,categorie,nomPlant);
	    
	        //  Le Optional contient toujours une valeur : 0 ou un vrai numéro
	        return ResponseEntity.ok(dernierNumeroCycle);
	    }
	    
	@GetMapping("/pistolets-par-pdek-et-page")
	public ResponseEntity<List<PistoletDTO>> getPistoletsParPdekEtPage(
	        @RequestParam Long pdekId,
	        @RequestParam int pageNumber) {

	    List<Pistolet> pistolets = pistoletRepository.findByPdekPistolet_IdAndPagePDEK_PageNumber(pdekId, pageNumber);

	    List<PistoletDTO> pistoletsDTOs = pistolets.stream().map(p ->
	        new PistoletDTO(
	          p.getId() ,
	          p.getDateCreation() ,
	          p.getTypePistolet() ,
	          p.getNumeroPistolet() ,
	          p.getLimiteInterventionMax() ,
	          p.getLimiteInterventionMin() ,
	          "R" ,
	          p.getCoupePropre() ,
	          p.getUserPistolet().getMatricule() ,
	          p.getEch1() ,
	          p.getEch2() ,
	          p.getEch3() ,
	          p.getEch4() ,
	          p.getEch5() ,
	          p.getMoyenne() ,
	          p.getEtendu(), 
	          p.getNumeroCycle() ,
	          p.getNbrCollierTester() ,
	          p.getAxeSerrage() ,
	          p.getSemaine() ,
	          p.getDecision()	,
	          p.getUserPistolet().getMatricule()
	        )
	    ).collect(Collectors.toList());

	    return ResponseEntity.ok(pistoletsDTOs);
	}
	
	 @GetMapping("/pistolets-non-validees")
	    public List<PistoletDTO> getPistoletsNonValidees() {
	        return pistoletService.getPistoletsNonValidees();
	    }
	 @GetMapping("/nbrNotifications")
	    public int getNombresNotificationsPistoletsNonValider() {
	        return pistoletService.getPistoletsNonValidees().size();
	    }
	
	 @GetMapping("/pistoles-validees")
	    public List<PistoletDTO> getPistoletsValidees() {
	        return pistoletService.getPistoletsValidees();
	    }
	
	 @PutMapping("/validerPistolet")
	 public ResponseEntity<?> validerPistolet(@RequestParam Long id) {
	     pistoletService.validerPistolet(id);
	     return ResponseEntity.ok().build();
	 }


}
