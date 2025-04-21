package rahma.backend.gestionPDEK.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plan_action")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dateCreation;
    private String heureCreation;
    @Enumerated(EnumType.STRING)
    private TypesOperation type_operation;

  

    // User qui a rempli le plan d'action
    @ManyToMany(mappedBy = "plansActionRemplis")
    private List<User> utilisateursRemplisseurs;

    // Page du PDEK concernée
    @OneToOne
    @JoinColumn(name = "page_pdek_id", unique = true)
    private PagePDEK pagePDEK;
    
    @OneToMany(mappedBy = "planAction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailsPlanAction> details = new ArrayList();

}
