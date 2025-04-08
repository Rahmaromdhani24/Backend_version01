package rahma.backend.gestionPDEK.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoudureDTO {
    private Long id;
    private String code;
    private String sectionFil;
    private String date;
    private int numeroCycle;
    private int userSoudure ;
    private double moyenne ; 
    private int etendu ;  

}
