package rahma.backend.gestionPDEK.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SertissageNormal_DTO {
    private Long id;
    private String code;
    private String sectionFil;
    private String numOutil ; 
    private String numContact ; 
    private String date;
    private int numCycle;
    private int userSertissageNormal ; 

}
