package rahma.backend.gestionPDEK.Controllers;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.ServicesImplementation.*;

@RestController
@RequestMapping("/operations")
public class OperationController {
    @Autowired
    private OperationService operationService;

    @GetMapping("/types")
    public List<String> getAllTypesOperations() {
        return Arrays.stream(TypesOperation.values())
                     .map(Enum::name)
                     .toList();
    }
    
    @GetMapping("/details")
    public List<Map<String, String>> getAllTypesOperationsWithDescriptions() {
        return Arrays.stream(TypesOperation.values())
                     .map(type -> Map.of(
                         "name", type.name(),
                         "description", type.getDescription()
                     ))
                     .toList();
    }

    
    @GetMapping("/CodesOperation/{operationName}")
    public List<String> getCodesControlesOperation( @PathVariable String operationName) {
       String nomOperation = operationName ;
       if(nomOperation.equals("Soudure"))
           return Soudure.CODES_CONTROLES; 
        if(nomOperation.equals("Torsadage"))
             return Torsadage.CODES_CONTROLES;
        if(nomOperation.equals("Sertissage_Normal"))
             return SertissageNormal.CODES_CONTROLES;
        if(nomOperation.equals("Sertissage_IDC"))
             return SertissageIDC.CODES_CONTROLES;
        return null  ; 
    }
    @GetMapping("/details/{operationName}")
    public Map<String, String> getCodesControlesDescriptions(@PathVariable String operationName) {
        String nomOperation = operationName ;
        if(nomOperation.equals("Soudure"))
        return new TreeMap<>(Soudure.CODES_CONTROLES_DESCRIPTION);
     if(nomOperation.equals("Torsadage"))
         return new TreeMap<>(Torsadage.CODES_CONTROLES_DESCRIPTION);
     if(nomOperation.equals("Sertissage_Normal"))
         return SertissageNormal.CODES_CONTROLES_DESCRIPTION;
    if(nomOperation.equals("Sertissage_IDC"))
         return SertissageIDC.CODES_CONTROLES_DESCRIPTION;
     return null  ; 
    }
}
 /*   @GetMapping("/sections-fil")
    public List<String> getSectionsFil() {
        return Arrays.stream(SectionFil_Soudure.values())
                     .map(SectionFil_Soudure::getValue)
                     .collect(Collectors.toList());
    }

    @GetMapping("/codes-controles")
    public List<String> getCodesControles() {
        return Arrays.stream(CodeControle_Soudure.values())
                     .map(CodeControle_Soudure::getCode)
                     .collect(Collectors.toList());
    }*/

