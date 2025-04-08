package rahma.backend.gestionPDEK.DTO;

import lombok.Getter;
import lombok.Setter;
import rahma.backend.gestionPDEK.Entity.PDEK;
import rahma.backend.gestionPDEK.Entity.TypePistolet;

@Getter
@Setter
public class PistoletDTO {

		private Long id;
	    private String date;
	    private TypePistolet typePistolet ; 
	    private int  numeroPistolet ; 
	    private String limiteInterventionMax ; 
	    private String limiteInterventionMin ; 
	    private PDEK pdek ;
	    private String codeRepartiton ; 
	    private String coupePropre ; 
	    private String matriculeAgentQualité ; 
	    private String ech1 ; 
	    private String ech2 ; 
	    private String ech3 ; 
	    private String ech4 ; 
	    private String ech5 ; 
	    private String moyenne ; 
	    private String ettendu ; 
        private String numCourant ; 
	    private String numCollierTester ; 
	    private String axeSerrage ; 
	    private String semaine ; 
	    private String decision ; 
	    
	    
		public PistoletDTO(Long id, String date, TypePistolet typePistolet, int numeroPistolet,
				 String limiteInterventionMax, String limiteInterventionMin, PDEK pdek) {
			super();
			this.id = id;
			this.date = date;
			this.typePistolet = typePistolet;
			this.numeroPistolet = numeroPistolet;
			this.limiteInterventionMax = limiteInterventionMax;
			this.limiteInterventionMin = limiteInterventionMin;
			this.pdek = pdek;
		}


		public PistoletDTO(String string, TypePistolet pistoletJaune, int i, String string2, String string3, PDEK pdek2,
				Object object, Object object2, String string4, String string5, String string6, String string7,
				String string8, String string9, String string10, String string11, String string12, String string13,
				String string14, String string15, String string16) {
			// TODO Auto-generated constructor stub
		} 
	    
	
	    
}
