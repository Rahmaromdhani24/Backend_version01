package rahma.backend.gestionPDEK.Controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rahma.backend.gestionPDEK.DTO.SertissageIDC_DTO;
import rahma.backend.gestionPDEK.DTO.SoudureDTO;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import rahma.backend.gestionPDEK.ServicesImplementation.SertissageIDC_ServiceImplimenetation;

@RestController
@RequestMapping("/operations/SertissageIDC")
public class SertissageIDCController {
	
	   
       @Autowired private SertissageIDC_ServiceImplimenetation serviceSertissageIDC ; 
       @Autowired private PdekRepository pdekRepository  ; 
       @Autowired private SertissageIDCRepository sertissageIDCRepository ; 

    @GetMapping("/sectionsFils")
    public List<String> getSectionsFils() {
        return SertissageIDC.SECTIONS_FILS;
    }

    @GetMapping("/codesControles")
    public List<String> getCodesControles() {
        return SertissageIDC.CODES_CONTROLES;
    }
    
    @GetMapping("/controle/{code}")
    public String getDescriptionForCode(@PathVariable String code) {
        return SertissageIDC.getDescriptionForCode(code);
    }
    @GetMapping("/findUniquePDEK")
    public ResponseEntity<?> findUniquePDEK_SertissageIDC(
            @RequestParam String sectionFilSelectionne,
            @RequestParam int segment,
            @RequestParam String nomPlant,
            @RequestParam String projetName) {
        try {
            Optional<PDEK> pdek = pdekRepository.findUniquePDEK_SertissageIDC(
                    sectionFilSelectionne, segment, Plant.valueOf(nomPlant), projetName);
            
            if (pdek.isPresent()) {
                return ResponseEntity.ok(pdek.get());  // Retourne l'objet PDEK trouvé
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun PDEK trouvé");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Erreur : Plant invalide - " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erreur serveur : " + e.getMessage());
        }
    }

    
    @PostMapping("/ajouterPdekSertissageIDC")
    public ResponseEntity<String> ajouterSertissageIDC(
    		 @RequestParam int matricule,
    		 @RequestParam String nomProjet,
             @RequestBody SertissageIDC sertissageIDC) {
        try {
        	serviceSertissageIDC.ajoutPDEK_SertissageIDC( sertissageIDC , matricule, nomProjet);
            return ResponseEntity.status(HttpStatus.CREATED).body("Sertissage IDC ajouté avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }



    @GetMapping("/dernier-numero-cycle")
    public ResponseEntity<?> getLastNumeroCycle(
            @RequestParam String sectionFil,
            @RequestParam int segment,
            @RequestParam Plant nomPlant,
            @RequestParam String projetName) {
    
        int dernierNumeroCycle = serviceSertissageIDC.getLastNumeroCycle(sectionFil, segment, nomPlant, projetName);
    
        //  Le Optional contient toujours une valeur : 0 ou un vrai numéro
        return ResponseEntity.ok(dernierNumeroCycle);
    }
    @GetMapping("/sertissages-par-pdek")
    public Map<Integer, List<SertissageIDC_DTO>> getSouduresParPdek(
            @RequestParam String sectionFil,
            @RequestParam String nomProjet,
            @RequestParam int segment,
            @RequestParam Plant plant) {
        return serviceSertissageIDC.recupererSertissagesParPDEKGroupéesParPage(sectionFil, segment, plant, nomProjet);
    }

}
