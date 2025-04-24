package rahma.backend.gestionPDEK.DTO;

import lombok.Getter;
import lombok.Setter;
import rahma.backend.gestionPDEK.Entity.CategoriePistolet;
import rahma.backend.gestionPDEK.Entity.PDEK;
import rahma.backend.gestionPDEK.Entity.TypePistolet;
import rahma.backend.gestionPDEK.Entity.TypesOperation;

@Getter
@Setter
public class PlanActionDTO {
	
	private Long id;
    private String dateCreation;
	private String heureCreation;
    private TypesOperation type_operation;
	private Long pagePdekId ;
	private Long pdekId ; 
	private int matriculeUser ; 

}
