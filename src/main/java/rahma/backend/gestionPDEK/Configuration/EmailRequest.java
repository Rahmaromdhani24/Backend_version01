package rahma.backend.gestionPDEK.Configuration;

import lombok.*;

@Setter
@Getter
public  class EmailRequest {
    private String toEmail;
    private String nomResponsable;
    private String nomProcess ; 
    private String posteErreur;
    private String descriptionErreur;
    private String localisation;
    private String valeurMesuree;      
    private String limitesAcceptables;  
}