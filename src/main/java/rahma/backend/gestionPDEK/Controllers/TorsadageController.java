package rahma.backend.gestionPDEK.Controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rahma.backend.gestionPDEK.DTO.AjoutTorsadageResultDTO;
import rahma.backend.gestionPDEK.DTO.PdekDTO;
import rahma.backend.gestionPDEK.DTO.SoudureDTO;
import rahma.backend.gestionPDEK.DTO.TorsadageDTO;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import rahma.backend.gestionPDEK.ServicesImplementation.PDEK_ServiceImplimenetation;
import rahma.backend.gestionPDEK.ServicesImplementation.TorsadageServiceImplimentation;

@RestController
@RequestMapping("/operations/torsadage")
public class TorsadageController {

	@Autowired  private TorsadageServiceImplimentation serviceTorsadage;
    @Autowired  private TorsadageRepository torsadageRepository ; 
    @Autowired  private PDEK_ServiceImplimenetation servicePDEK ; 
    @Autowired  private PdekRepository repositoryPDEK ; 


    @GetMapping("/specificationsMesure")
    public List<String> getSectionsFils() {
        return Torsadage.SPECIFICATIONS_MESURES;
    }

    @GetMapping("/codesControles")
    public List<String> getCodesControles() {
        return Torsadage.CODES_CONTROLES;
    }
    
    @GetMapping("/controle/{code}")
    public String getDescriptionForCode(@PathVariable String code) {
        return Torsadage.getDescriptionForCode(code);
    }

    /******************************* Partie PDEK *******************************/
     @GetMapping("/verifier-pdek")
    public boolean verifierPDEK(
            @RequestParam String specificationMesure,
            @RequestParam int  segment,
            @RequestParam Plant nomPlant,
            @RequestParam String nomProjet) {
        return servicePDEK.verifierExistencePDEK_Torsadage(specificationMesure,segment , nomPlant ,  nomProjet);
    }
    
    @GetMapping("/pdekExiste")
    public PdekDTO recupererNumCycleSiPDEKExist(
    		  @RequestParam String specificationMesure,
              @RequestParam int  segment,
              @RequestParam Plant nomPlant,
              @RequestParam String nomProjet) {
    	return  servicePDEK.recupererPdekTorsadag(specificationMesure, segment , nomPlant , nomProjet);     
    }
    

    @GetMapping("/torsadage-par-pdek")
    public Map<Integer, List<TorsadageDTO>>  getTorsadagesParPdek(
            @RequestParam String specificationMesure,
            @RequestParam String nomProjet,
            @RequestParam int segment,
            @RequestParam Plant plant) {
        return serviceTorsadage.recupererTorsadagesParPDEKGroupéesParPage(specificationMesure,segment , plant ,  nomProjet);
    }
    
     @PostMapping("/ajouterPDEK")
    public ResponseEntity<String> ajouterTorsadageAvecPdek(
            @RequestBody Torsadage torsadage, 
            @RequestParam int matriculeOperateur, 
            @RequestParam String projet) {

        try {
        AjoutTorsadageResultDTO result = serviceTorsadage.ajoutPDEK_Torsadage(torsadage, matriculeOperateur, projet);
           String jsonResponse = "{ \"pdekId\": \"" + result.getPdekId() + "\", \"pageNumber\": \"" + result.getNumeroPage() + "\" }";
           return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'ajout : " + e.getMessage());
        }
    }


     @GetMapping("/dernier-numero-cycle")
     public ResponseEntity<?> getLastNumeroCycle(
             @RequestParam String specificationMesureSelectionner,
             @RequestParam int segment,
             @RequestParam Plant nomPlant,
             @RequestParam String projetName) {
     
         int dernierNumeroCycle = serviceTorsadage.getLastNumeroCycle(specificationMesureSelectionner, segment, nomPlant, projetName);
     
         //  Le Optional contient toujours une valeur : 0 ou un vrai numéro
         return ResponseEntity.ok(dernierNumeroCycle);
     }
     @GetMapping("/page-actuelle")
     public ResponseEntity<List<TorsadageDTO>> getTorsadagesParPageActuelle(
             @RequestParam String specificationMesure,
             @RequestParam int segment,
             @RequestParam String plant, // à convertir si `Plant` est un enum ou objet
             @RequestParam String nomProjet
     ) {
         try {
             // Si Plant est un enum :
             Plant plantEnum = Plant.valueOf(plant.toUpperCase());
 
             List<TorsadageDTO> liste = serviceTorsadage.recupererTorsadagesParPageActuel(
                     specificationMesure, segment, plantEnum, nomProjet
             );
 
             return ResponseEntity.ok(liste);
         } catch (IllegalArgumentException e) {
             return ResponseEntity.badRequest().build(); // Mauvais nom de plant
         }
     }
     @GetMapping("/torsadages-par-pdek-et-page")
public ResponseEntity<List<TorsadageDTO>> getTorsadageParPdekEtPage(
        @RequestParam Long pdekId,
        @RequestParam int pageNumber) {

    List<Torsadage> torsadages = torsadageRepository.findByPdekTorsadage_IdAndPagePDEK_PageNumber(pdekId, pageNumber);

    List<TorsadageDTO> soudureDTOs = torsadages.stream().map(s ->
        new TorsadageDTO(
            s.getId(),
            s.getCode(),
            s.getSpecificationMesure(),
            s.getDate(),
            s.getNumeroCycle(),
            s.getUserTorsadage().getMatricule(),
            s.getMoyenne(),
            s.getEtendu() 
        )
    ).collect(Collectors.toList());

    return ResponseEntity.ok(soudureDTOs);
}


 }

