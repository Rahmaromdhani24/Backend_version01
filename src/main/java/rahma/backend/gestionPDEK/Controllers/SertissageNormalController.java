package rahma.backend.gestionPDEK.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rahma.backend.gestionPDEK.DTO.SertissageNormal_DTO;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import rahma.backend.gestionPDEK.ServicesImplementation.PDEK_ServiceImplimenetation;
import rahma.backend.gestionPDEK.ServicesImplementation.SertissageNormalServiceImplimentation;

@RestController
@RequestMapping("/operations/SertissageNormal")
public class SertissageNormalController {
	   @Autowired private SertissageNormalRepository sertissageNormalRepository ; 
       @Autowired private PDEK_ServiceImplimenetation servicePDEK ; 
       @Autowired  private  SertissageNormalServiceImplimentation serviceSertissageNormal;

      

    @GetMapping("/codesControles")
    public List<String> getCodesControles() {
        return SertissageNormal.CODES_CONTROLES;
    }
    
    @GetMapping("/controle/{code}")
    public String getDescriptionForCode(@PathVariable String code) {
        return SertissageNormal.getDescriptionForCode(code);
    }
 

   

/********************** Nouvelle partie *********************/
@PostMapping("/ajouterPdekSertissageNormal")
public ResponseEntity<String> ajouterSertissageNormal(
         @RequestParam int matricule,
         @RequestParam String nomProjet,
         @RequestBody SertissageNormal sertissageNormal) {
    try {
        serviceSertissageNormal.ajoutPDEK_SertissageNormal( sertissageNormal , matricule, nomProjet);
        return ResponseEntity.status(HttpStatus.CREATED).body("Sertissage IDC ajouté avec succès");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
    }
}

  @GetMapping("/contacts")
  public List<String> getDistinctContacts(@RequestParam String numeroOutil) {
      return serviceSertissageNormal.getDistinctContactsByNumeroOutil(numeroOutil);
  }
      @GetMapping("/sections")
      public ResponseEntity<List<String>> getSectionsFil(@RequestParam String numeroOutil, @RequestParam String numeroContact) {
    	    List<String> sectionsFil = serviceSertissageNormal.getSectionsByOutilAndContact(numeroOutil, numeroContact);
    	    if (sectionsFil.isEmpty()) {
    	        return ResponseEntity.notFound().build();
    	    }
    	    return ResponseEntity.ok(sectionsFil);
    	}
      
      @GetMapping("/hauteurSertissage")
      public ResponseEntity<String> getHauteurSertissage(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String hauteurSertissage = serviceSertissageNormal.getHauteurSertissage(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      
      
      @GetMapping("/largeurSertissage")
      public ResponseEntity<String> getLargeurSertissage(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String hauteurSertissage = serviceSertissageNormal.getLargeurSertissage(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/ToleranceLargeurSertissage")
      public ResponseEntity<String> getToleranceLargeurSertissage(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String hauteurSertissage = serviceSertissageNormal.getToleranceLargeurSertissage(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/hauteurIsolant")
      public ResponseEntity<String> getHauteurIsolant(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur isolant
              String hauteurSertissage = serviceSertissageNormal.getHauteurIsolant(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/ToleranceHauteurIsolant")
      public ResponseEntity<String> getToleranceHauteurIsolant(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String hauteurSertissage = serviceSertissageNormal.getToleranceHauteurIsolant(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/largeurIsolant")
      public ResponseEntity<String> getLargeurIsolant(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String hauteurSertissage = serviceSertissageNormal.getLargeurIsolant(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/ToleranceLargeurIsolant")
      public ResponseEntity<String> getToleranceLargeurIsolant(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String hauteurSertissage = serviceSertissageNormal.getToleranceLargeurIsolant(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(hauteurSertissage); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/traction")
      public ResponseEntity<String> getTractionValeur(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la valeur de traction
              String traction = serviceSertissageNormal.getTractionValeur(numeroOutil, numeroContact, sectionFil);
      
              return ResponseEntity.ok()
                      .contentType(MediaType.TEXT_PLAIN) // Empêche l'encodage HTML
                      .body(traction);
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND)
                      .contentType(MediaType.TEXT_PLAIN)
                      .body(e.getMessage());
          }
      }
      @GetMapping("/tolerance")
      public ResponseEntity<String> getToleranceValue(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la valeur de traction
              String traction = serviceSertissageNormal.getToleranceValue(numeroOutil, numeroContact, sectionFil);
      
              return ResponseEntity.ok()
                      .contentType(MediaType.TEXT_PLAIN) // Empêche l'encodage HTML
                      .body(traction);
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND)
                      .contentType(MediaType.TEXT_PLAIN)
                      .body(e.getMessage());
          }
      }
      @GetMapping("/lgd")
      public ResponseEntity<String> getLgd(
              @RequestParam String numeroOutil,
              @RequestParam String numeroContact,
              @RequestParam String sectionFil) {
          
          try {
              // Appel du service pour récupérer la hauteur de sertissage
              String valueLGD = serviceSertissageNormal.getLGDeValue(numeroOutil, numeroContact, sectionFil);
              return ResponseEntity.ok(valueLGD); // Retourne la hauteur de sertissage
          } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Retourne une erreur en cas d'exception
          }
      }
      @GetMapping("/dernier-numero-cycle")
    public ResponseEntity<?> getLastNumeroCycle(
            @RequestParam String sectionFil,
            @RequestParam int segment,
            @RequestParam Plant nomPlant,
            @RequestParam String projetName) {
    
        int dernierNumeroCycle = serviceSertissageNormal.getLastNumeroCycle(sectionFil, segment, nomPlant, projetName);
    
        //  Le Optional contient toujours une valeur : 0 ou un vrai numéro
        return ResponseEntity.ok(dernierNumeroCycle);
    }
 @GetMapping("/sertissageNormal-par-pdek")
    public Map<Integer, List<SertissageNormal_DTO>> getSertissagesNormalParPdek(
            @RequestParam String sectionFil,
            @RequestParam String nomProjet,
            @RequestParam int segment,
            @RequestParam Plant plant) {
        return serviceSertissageNormal.recupererSertissagesNormalesParPDEKGroupéesParPage(sectionFil ,segment, plant, nomProjet);
    }
  }

