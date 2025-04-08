package rahma.backend.gestionPDEK.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TorsadageDTO {
    private Long id;
    private String code;
    private String specificationMesure;
    private String date;
    private int numeroCycle;
    private int userTorsadage ; 
    private  double moyenne ; 
    private int etendu ; 



}
