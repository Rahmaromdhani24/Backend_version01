package rahma.backend.gestionPDEK.ServicesInterfaces;

import rahma.backend.gestionPDEK.DTO.PdekDTO;
import rahma.backend.gestionPDEK.DTO.PistoletDTO;
import rahma.backend.gestionPDEK.Entity.CategoriePistolet;
import rahma.backend.gestionPDEK.Entity.Plant;
import rahma.backend.gestionPDEK.Entity.TypePistolet;
import rahma.backend.gestionPDEK.Entity.TypesOperation;

public interface PDEKService {
	
										/****** Soudure utlrason *****/

	public boolean verifierExistencePDEK_soudureUltrason(String sectionFil, int segment ,Plant plant , String nomProjet ) ; 
	public PdekDTO recupererPdekSoudureUltrason(String sectionFil, int segment ,Plant plant , String nomProjet) ; 
	
	
	
                                      	/****** torsadage *****/
	public boolean verifierExistencePDEK_Torsadage(String sectionFil, int segment ,Plant plant , String nomProjet ) ; 
	public PdekDTO recupererPdekTorsadag(String sectionFil, int segment ,Plant plant , String nomProjet) ; 
	

	                                	/****** torsadage *****/
	public boolean verifierExistencePDEK_Pistolet(TypePistolet typePistolet , CategoriePistolet categoriePistolet , int numeroPistolet ) ; 
	public PistoletDTO recupererPdek_Pistolet(TypePistolet typePistolet , CategoriePistolet categoriePistolet , int numeroPistolet ) ; 
	


  	/****** torsadage *****/
/*public boolean verifierExistencePDEK(String sectionFil, int segment ,Plant plant , String nomProjet ) ; 
public PdekDTO recupererPdekDTO(String sectionFil, int segment ,Plant plant , String nomProjet) ; 



public boolean verifierExistencePDEK(String sectionFil, int segment ,Plant plant , String nomProjet ) ; 
public PdekDTO recupererPdekDTO(String sectionFil, int segment ,Plant plant , String nomProjet) ; 


public boolean verifierExistencePDEK(String sectionFil, int segment ,Plant plant , String nomProjet ) ; 
public PdekDTO recupererPdekDTO(String sectionFil, int segment ,Plant plant , String nomProjet) ; */


	
	
	
}
