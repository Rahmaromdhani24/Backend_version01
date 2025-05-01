package rahma.backend.gestionPDEK.Controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rahma.backend.gestionPDEK.DTO.AjoutTorsadageResultDTO;
import rahma.backend.gestionPDEK.DTO.OperateurErreurDTO;
import rahma.backend.gestionPDEK.DTO.PdekDTO;
import rahma.backend.gestionPDEK.DTO.TorsadageDTO;
import rahma.backend.gestionPDEK.DTO.UserDTO;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import rahma.backend.gestionPDEK.ServicesImplementation.PDEK_ServiceImplimenetation;
import rahma.backend.gestionPDEK.ServicesImplementation.TorsadageServiceImplimentation;
import rahma.backend.gestionPDEK.ServicesInterfaces.StatistiquesService;

@RestController
@RequestMapping("/statistiques")
public class StatistiquesController {

 
    @Autowired  private StatistiquesService service ; 


/*************************************** All process sauf pistolet **********************************/
    
    @GetMapping("/nombre-operateurs")
    public ResponseEntity<Long> getAllOperateurs() {
        return ResponseEntity.ok(service.nombreTotalOperateurs());
    }

    @GetMapping("/nombre-operateurs-hommes")
    public ResponseEntity<Long> getNombreHommesOperateurs() {
        return ResponseEntity.ok(service.nombreHommesOperateurs());
    }

    @GetMapping("/nombre-operateurs-femmes")
    public ResponseEntity<Long> getNombreFemmesOperateurs() {
        return ResponseEntity.ok(service.nombreFemmesOperateurs());
    }

    @GetMapping("/pourcentage-augmentation")
    public ResponseEntity<Double> getPourcentageAugmentation() {
    	double pourcentage = service.calculerPourcentageAugmentationOperateurs();
        return ResponseEntity.ok(pourcentage);
    }

    @GetMapping("/erreurs-semaine")
    public ResponseEntity<Long> getErreursAllProcessCetteSemaine() {
        long count = service.nombreErreursTotalCetteSemaineSaufPistolet();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/pourcentage-erreurs-semaine")
    public ResponseEntity<Double> getPourcentagesErreursAllProcessCetteSemaine() {
    	double count = service.calculerPourcentageSemainePrecdant();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/pdek-count-by-type")
    public ResponseEntity<Map<String, Long>> getPdekCountByTypeOperation() {
        Map<String, Long> stats = service.getNombrePdekParTypeOperation();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/planAction-count-by-type")
    public ResponseEntity<Map<String, Long>> getPlanActionCountByTypeOperation() {
        Map<String, Long> stats = service.getNombrePlanActionParTypeOperation() ;
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/top5-operateurs-erreurs")
    public ResponseEntity<List<OperateurErreurDTO>> getTop5OperateursWithErrors() {
        return ResponseEntity.ok(service.getTop5OperateursWithErrors());
    }

 }

 

