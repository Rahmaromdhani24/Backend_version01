package rahma.backend.gestionPDEK.ServicesImplementation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import rahma.backend.gestionPDEK.DTO.AjoutPistoletResultDTO;
import rahma.backend.gestionPDEK.DTO.PistoletDTO;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import rahma.backend.gestionPDEK.ServicesInterfaces.ServicePistolet;

@Service
@RequiredArgsConstructor
public class PistoletServiceImplimenetation  implements ServicePistolet {

    @Autowired private PistoletRepository pistoletRepository;
    @Autowired private PdekRepository pdekRepository;
    @Autowired private PdekPageRepository pagePDEKRepository;
    @Autowired private UserRepository userRepository; 
   @Autowired  private ControleQualiteRepository controleQualiteRepository;

    @Transactional
    public AjoutPistoletResultDTO ajouterPistolet(int matricule, Pistolet pistolet) {
        // 1. Vérifier si l'utilisateur existe
        User user = userRepository.findById(matricule)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 2. Vérifier si un PDEK existe pour le type de pistolet
        PDEK pdek = pdekRepository.findUniquePDEK_MontagePistolet(pistolet.getType() ,user.getSegment() , pistolet.getNumeroPistolet() , pistolet.getCategorie() , user.getPlant() )
                .orElseGet(() -> {
                    PDEK newPdek = new PDEK();
                    newPdek.setTypePistolet(pistolet.getType());
                    newPdek.setDateCreation(pistolet.getDateCreation());
                    newPdek.setNombreEchantillons("5 Piéces") ; 
                    newPdek.setPlant(user.getPlant())  ; 
                    newPdek.setSegment(user.getSegment())  ; 
                    newPdek.setTypeOperation(TypesOperation.Montage_Pistolet) ;  
                    newPdek.setNumeroPistolet(pistolet.getNumeroPistolet()) ; 
                    newPdek.setCategoriePistolet(pistolet.getCategorie()) ; 
                    newPdek.setTotalPages(1);
                    return pdekRepository.save(newPdek);

                });

        // 3. Trouver la dernière page du PDEK
        PagePDEK pagePDEK = pagePDEKRepository.findFirstByPdekOrderByPageNumberDesc(pdek)
                .orElseGet(() -> {
                    PagePDEK newPage = new PagePDEK(1, false, pdek);
                    pagePDEKRepository.save(newPage);
                    return newPage;
                });

        // 4. Compter le nombre de pistolets sur la page actuelle
        long nombrePistoletsDansPage = pistoletRepository.countByPagePDEK(pagePDEK);
        int numeroCycle;

        if (nombrePistoletsDansPage < 25) {
            // Ajouter le pistolet à la même page
            numeroCycle = (int) nombrePistoletsDansPage + 1;
        } else {
            // Si la page est pleine, créer une nouvelle page
            pagePDEK = new PagePDEK(pdek.getTotalPages() + 1, false, pdek);
            pagePDEKRepository.save(pagePDEK);

            // Mettre à jour le total de pages du PDEK
            pdek.setTotalPages(pdek.getTotalPages() + 1);
            pdekRepository.save(pdek);

            numeroCycle = 1; // Réinitialiser le cycle pour la nouvelle page
        }

        // 5. Associer le Pistolet au PDEK et à l'utilisateur
        pistolet.setPdekPistolet(pdek);
        pistolet.setPagePDEK(pagePDEK);
        pistolet.setNumeroCycle(numeroCycle);
        pistolet.setUserPistolet(user); 
        pistolet.setSegment(user.getSegment()); 
        pistoletRepository.save(pistolet);

        // 6. Associer l'utilisateur au PDEK pour le remplissage (ManyToMany)
        if (pdek.getUsersRempliePDEK() == null) {
            pdek.setUsersRempliePDEK(new ArrayList<>());
        }

        if (!pdek.getUsersRempliePDEK().contains(user)) {
            pdek.getUsersRempliePDEK().add(user);
            pdekRepository.save(pdek);
        }

		return new AjoutPistoletResultDTO(pdek.getId(), pagePDEK.getPageNumber());
    }
    /*********************************************************************************************************************/
    public int getLastNumeroCycle(String typePistolet, int segment, int numPistolet ,String categorie , Plant nomPlant) {
		 // 1️⃣ Récupérer le PDEK correspondant
		 Optional<PDEK> pdekOpt = pdekRepository.findUniquePDEK_MontagePistolet(TypePistolet.valueOf(typePistolet) ,segment , numPistolet , CategoriePistolet.valueOf(categorie) ,nomPlant);
	 
		 if (pdekOpt.isEmpty()) {
			 // Aucun PDEK trouvé → retourner 0
			 return 0;
		 }
	 
		 PDEK pdek = pdekOpt.get();
	 
		 // 2️⃣ Récupérer la dernière page associée au PDEK
		 Optional<PagePDEK> lastPageOpt = pagePDEKRepository.findFirstByPdekOrderByPageNumberDesc(pdek);
	 
		 if (lastPageOpt.isEmpty()) {
			 // Le PDEK existe, mais aucune page n'est encore créée → retourner 0
			 return 0;
		 }
	 
		 PagePDEK lastPage = lastPageOpt.get();
	 
		 // 3️⃣ Vérifier s'il existe des soudures dans cette page
		 long nombrePistoletDansPage = pistoletRepository.countByPagePDEK(lastPage);
	 
		 if (nombrePistoletDansPage == 0) {
			 // Si la page est vide, retourner 0
			 return 0;
		 }
	 
		 // 4️⃣ Récupérer le dernier numéro de cycle
		 Optional<Pistolet> lastPistoletOpt = pistoletRepository.findTopByPagePDEK_IdOrderByNumeroCycleDesc(lastPage.getId());
	 
		 if (lastPistoletOpt.isPresent()) {
			 // Si une soudure est présente, retourner son numéro de cycle
			 return lastPistoletOpt.get().getNumeroCycle();
		 }
	 
		 // Si aucune soudure n'est trouvée malgré les vérifications, retourner 0
		 return 0;
	 }
    @Override
    public List<PistoletDTO> getPistoletsNonValidees() {
        List<Pistolet> pistolets = pistoletRepository.findByDecision(0);

        return pistolets.stream()
            .map(p -> new PistoletDTO( 
      	        p.getId() ,
      	        p.getPdekPistolet().getId()  ,
      	        p.getPagePDEK().getPageNumber() ,
      	        p.getSegment() ,
                p.getDateCreation(),
                p.getHeureCreation() ,
                p.getTypePistolet(),
                p.getNumeroPistolet(),
                p.getLimiteInterventionMax(),
                p.getLimiteInterventionMin(),
                "R",
                p.getCoupePropre(),
                p.getUserPistolet().getMatricule(),
                p.getEch1(),
                p.getEch2(),
                p.getEch3(),
                p.getEch4(),
                p.getEch5(),
                p.getMoyenne(),
                p.getEtendu(), 
  	            p.getCategorie() , 
                p.getNumeroCycle(),
                p.getNbrCollierTester(),
                p.getAxeSerrage(),
                p.getSemaine(),
                p.getDecision(),
                p.getUserPistolet().getMatricule()
            ))
            .toList();
    }


    @Override
    public List<PistoletDTO> getPistoletsValidees() {
        List<Pistolet> pistolets = pistoletRepository.findByDecision(1);

        return pistolets.stream()
            .map(p -> new PistoletDTO(  
      	        p.getId() ,  
      	        p.getPdekPistolet().getId()  ,
      	        p.getPagePDEK().getPageNumber() ,
      	        p.getSegment() ,
                p.getDateCreation(),
                p.getHeureCreation() ,
                p.getTypePistolet(),
                p.getNumeroPistolet(),
                p.getLimiteInterventionMax(),
                p.getLimiteInterventionMin(),
                "R",
                p.getCoupePropre(),
                p.getUserPistolet().getMatricule(),
                p.getEch1(),
                p.getEch2(),
                p.getEch3(),
                p.getEch4(),
                p.getEch5(),
                p.getMoyenne(),
                p.getEtendu(), 
  	            p.getCategorie() , 
                p.getNumeroCycle(),
                p.getNbrCollierTester(),
                p.getAxeSerrage(),
                p.getSemaine(),
                p.getDecision(),
                p.getUserPistolet().getMatricule()
            ))
            .toList();
    }
	@Override
	public void validerPistolet(Long idPistolet, int matriculeUser) {
		   String heure = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
		   String date  = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

	    // Valider le pistolet
	    pistoletRepository.validerPistolet(idPistolet);

	    // Récupérer le pistolet concerné
	    Pistolet pistolet = pistoletRepository.findById(idPistolet)
	        .orElseThrow(() -> new RuntimeException("Pistolet non trouvé avec ID : " + idPistolet));

	    // Récupérer le PDEK associé
	    PDEK pdek = pistolet.getPdekPistolet() ; 

	    // Récupérer l'utilisateur via son matricule
	    User userControleur = userRepository.findByMatricule(matriculeUser)
	        .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec matricule : " + matriculeUser));

	    // Créer l'entrée de contrôle qualité
	    ControleQualite controle = ControleQualite.builder()
	        .user(userControleur)
	        .pdek(pdek)
	        .idInstanceOperation(pistolet.getId())
	        .nombrePage(pdek.getPages() != null ? pdek.getPages().size() : 0)
	        .dateControle(date)
	        .heureControle(heure)
	        .resultat("Validé")
	        .build();

	    // Sauvegarder le contrôle qualité
	    controleQualiteRepository.save(controle);
	}

}
