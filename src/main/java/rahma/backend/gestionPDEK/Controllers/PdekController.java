package rahma.backend.gestionPDEK.Controllers;

import rahma.backend.gestionPDEK.ServicesImplementation.PDEK_ServiceImplimenetation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import rahma.backend.gestionPDEK.DTO.ContenuPagePdekDTO;
import rahma.backend.gestionPDEK.DTO.PdekResultat;
import rahma.backend.gestionPDEK.Entity.PDEK;
import rahma.backend.gestionPDEK.Entity.TypesOperation;
import rahma.backend.gestionPDEK.Entity.User;
import rahma.backend.gestionPDEK.Repository.PdekRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pdek")
@RequiredArgsConstructor
public class PdekController {

	 @Autowired  private  PDEK_ServiceImplimenetation  pdekService;
	 @Autowired  private  PdekRepository pdekRepository ;
	 
	 @GetMapping("/pdeks/{typeOperation}")
	 public List<PdekResultat> getPdekLightByTypeOperation(@PathVariable String typeOperation) {
	     try {
	         TypesOperation operationEnum = TypesOperation.valueOf(typeOperation);
	         List<PDEK> pdeks = pdekRepository.findByTypeOperation(operationEnum);

	         return pdeks.stream().map(pdek ->
	         new PdekResultat(
	             pdek.getId(),
	             pdek.getNumeroPistolet() ,
	             pdek.getTypeOperation(),
	             pdek.getTypePistolet(),
	             pdek.getCategoriePistolet(),
	             pdek.getPlant(),
	             pdek.getSegment(),
	             pdek.getTotalPages(),
	             pdek.getUsersRempliePDEK() != null
	                 ? pdek.getUsersRempliePDEK().stream()
	                     .map(user -> user.getMatricule() + " - " + user.getNom() + " " + user.getPrenom())
	                     .toList()
	                 : List.of()
	         )
	     ).toList();



	     } catch (IllegalArgumentException e) {
	         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type d'opération invalide : " + typeOperation);
	     }
	 }


	 @GetMapping("/contenu-par-page/{id}")
	 public ResponseEntity<List<ContenuPagePdekDTO>> getContenuParPage(@PathVariable Long id) {
	     List<ContenuPagePdekDTO> contenu = pdekService.getContenuParPage(id);
	     return ResponseEntity.ok(contenu);
	 }

	 @GetMapping("/{id}")
	 public ResponseEntity<?> getPdekById(@PathVariable Long id) {
	     Object dto = pdekService.getPdekDTOById(id);
	     return ResponseEntity.ok(dto);
	 }


}
