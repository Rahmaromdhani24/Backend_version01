package rahma.backend.gestionPDEK.ServicesImplementation;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;

@Service
@RequiredArgsConstructor
public class PistoletServiceImplimenetation {

    @Autowired private PistoletRepository pistoletRepository;
    @Autowired private PdekRepository pdekRepository;
    @Autowired private PdekPageRepository pagePDEKRepository;
    @Autowired private UserRepository userRepository; 

    @Transactional
    public Pistolet ajouterPistolet(int matricule, Pistolet pistolet) {
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
        Pistolet savedPistolet = pistoletRepository.save(pistolet);

        // 6. Associer l'utilisateur au PDEK pour le remplissage (ManyToMany)
        if (pdek.getUsersRempliePDEK() == null) {
            pdek.setUsersRempliePDEK(new ArrayList<>());
        }

        if (!pdek.getUsersRempliePDEK().contains(user)) {
            pdek.getUsersRempliePDEK().add(user);
            pdekRepository.save(pdek);
        }

        return savedPistolet;
    }
    
	 ///////
	 /*public Optional<Integer> getLastNumeroCycle(String sectionFilSelectionne, int segment, Plant nomPlant) {
	        // 1️⃣ Récupérer le PDEK correspondant
	        Optional<PDEK> pdekOpt = pdekRepository.findUniquePDEK_MontagePistolet(sectionFilSelectionne, segment, nomPlant);

	        if (pdekOpt.isEmpty()) {
	            return Optional.empty(); // Aucun PDEK trouvé
	        }

	        PDEK pdek = pdekOpt.get();

	        // 2️⃣ Récupérer la dernière page PDEK
	        Optional<PagePDEK> lastPageOpt = pagePDEKRepository.findLastPageByPdek(pdek.getId());

	        if (lastPageOpt.isEmpty()) {
	            return Optional.empty(); // Aucune page trouvée
	        }

	        PagePDEK lastPage = lastPageOpt.get();

	        //  Récupérer le dernier numéro de cycle de sertissage normal
	        return pistoletRepository.findLastNumCycleByPage(lastPage.getId());
	    }*/
}
