package rahma.backend.gestionPDEK.DTO;

import lombok.Getter;
import lombok.Setter;
import rahma.backend.gestionPDEK.Entity.PDEK;
import rahma.backend.gestionPDEK.Entity.TypePistolet;

@Getter
@Setter
public class PistoletDTO {

		private Long id;
		private int segment ;
	    private String dateCreation;
	    private TypePistolet typePistolet ; 
	    private int  numeroPistolet ; 
	    private String limiteInterventionMax ; 
	    private String limiteInterventionMin ; 
	    private PDEK pdek ;
	    private String codeRepartiton ; 
	    private String coupePropre ; 
	    private int matriculeAgentQualité ; 
	    private int ech1 ; 
	    private int ech2 ; 
	    private int ech3 ; 
	    private int ech4 ; 
	    private int ech5 ; 
	    private double moyenne ; 
	    private int etendu ; 
        private int numCourant ; 
	    private String nbrCollierTester ; 
	    private int axeSerrage ; 
	    private int semaine ; 
	    private int decision ; 
	    private int matricule ; 
	    
	    
		public PistoletDTO(Long id, String dateCreation, TypePistolet typePistolet, int numeroPistolet,
				 String limiteInterventionMax, String limiteInterventionMin, PDEK pdek) {
			super();
			this.id = id;
			this.dateCreation = dateCreation;
			this.typePistolet = typePistolet;
			this.numeroPistolet = numeroPistolet;
			this.limiteInterventionMax = limiteInterventionMax;
			this.limiteInterventionMin = limiteInterventionMin;
			this.pdek = pdek;
		}


		public PistoletDTO(Long id  , int segment ,  String dateCreation, TypePistolet typePistolet, int numeroPistolet, String limiteInterventionMax,
				String limiteInterventionMin, String codeRepartiton, String coupePropre, int matriculeAgentQualité,
				int ech1, int ech2, int ech3, int ech4, int ech5, double moyenne, int etendu,
				int numCourant, String nbrCollierTester, int axeSerrage, int semaine, int decision , int matricule) {
			this.id =id ; 
			this.segment =segment  ; 
			this.dateCreation = dateCreation;
			this.typePistolet = typePistolet;
			this.numeroPistolet = numeroPistolet;
			this.limiteInterventionMax = limiteInterventionMax;
			this.limiteInterventionMin = limiteInterventionMin;
			this.codeRepartiton = codeRepartiton;
			this.coupePropre = coupePropre;
			this.matriculeAgentQualité = matriculeAgentQualité;
			this.ech1 = ech1;
			this.ech2 = ech2;
			this.ech3 = ech3;
			this.ech4 = ech4;
			this.ech5 = ech5;
			this.moyenne = moyenne;
			this.etendu = etendu;
			this.numCourant = numCourant;
			this.nbrCollierTester = nbrCollierTester;
			this.axeSerrage = axeSerrage;
			this.semaine = semaine;
			this.decision = decision;
			this.matricule= matricule ; 
					
		}


		


	

	
	
	    
}
