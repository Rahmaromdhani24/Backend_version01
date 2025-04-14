package rahma.backend.gestionPDEK.ServicesImplementation;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rahma.backend.gestionPDEK.DTO.AjoutSoudureResultDTO;
import rahma.backend.gestionPDEK.DTO.SoudureDTO;
import rahma.backend.gestionPDEK.Entity.*;
import rahma.backend.gestionPDEK.Repository.*;
import rahma.backend.gestionPDEK.ServicesInterfaces.ServiceSoudure;

@Service
public class SoudureServiceImplimentation implements ServiceSoudure {

	 @Autowired    private PdekRepository pdekRepository;
	 @Autowired    private SoudureRepository soudureRepository;	
	 @Autowired    private UserRepository userRepository;	
	 @Autowired    private ProjetRepository projetRepository;	
	 @Autowired    private PdekPageRepository pdekPageRepository;	

	 
	 @Override
	 public AjoutSoudureResultDTO ajoutPDEKSoudure(Soudure instanceSoudure, int matriculeOperateur, String projet) {

		String sectionFilSelectionner = instanceSoudure.getSectionFil();
		Optional<User> optionalUser = userRepository.findByMatricule(matriculeOperateur);
	
		if (optionalUser.isEmpty()) {
			throw new RuntimeException("Utilisateur avec matricule " + matriculeOperateur + " non trouvé.");
		}
	
		User user = optionalUser.get();
		Soudure instance1 = new Soudure();
	
		// Copier les champs
		instance1.setCode(instanceSoudure.getCode());
		instance1.setSectionFil(sectionFilSelectionner);
		instance1.setDate(instanceSoudure.getDate());
		instance1.setDistanceBC(instanceSoudure.getDistanceBC());
		instance1.setGrendeurLot(instanceSoudure.getGrendeurLot());
		instance1.setLimitePelage(instanceSoudure.getLimitePelage());
		instance1.setNombreKanban(instanceSoudure.getNombreKanban());
		instance1.setNombreNoeud(instanceSoudure.getNombreNoeud());
		instance1.setPelageX1(instanceSoudure.getPelageX1());
		instance1.setPelageX2(instanceSoudure.getPelageX2());
		instance1.setPelageX3(instanceSoudure.getPelageX3());
		instance1.setPelageX4(instanceSoudure.getPelageX4());
		instance1.setPelageX5(instanceSoudure.getPelageX5());
		instance1.setPliage(instanceSoudure.getPliage());
		instance1.setQuantiteAtteint(instanceSoudure.getQuantiteAtteint());
		instance1.setTraction(instanceSoudure.getTraction() + "N");
		instance1.setEtendu(instanceSoudure.getEtendu());
		instance1.setMoyenne(instanceSoudure.getMoyenne());
		instance1.setUserSoudure(user);
	
		Optional<PDEK> optionalPdek = pdekRepository.findUniquePDEK_SoudureUtrason(
				sectionFilSelectionner, user.getSegment(), user.getPlant(), projet
		);
	
		if (optionalPdek.isPresent()) {
			// ⚙️ Cas PDEK existe déjà
			PDEK pdek = optionalPdek.get();
	
			// Ajout de l'utilisateur et du projet s'ils ne sont pas déjà là
			if (!pdek.getUsersRempliePDEK().contains(user)) {
				pdek.getUsersRempliePDEK().add(user);
			}
	
			Projet projetEntity = projetRepository.findByNom(projet)
					.orElseThrow(() -> new RuntimeException("Projet " + projet + " non trouvé"));
			if (!pdek.getProjets().contains(projetEntity)) {
				pdek.getProjets().add(projetEntity);
			}
	
			// 🔄 Gestion de la pagination
			PagePDEK pagePDEK = pdekPageRepository.findFirstByPdekOrderByPageNumberDesc(pdek)
					.orElseThrow(() -> new RuntimeException("Aucune page trouvée pour ce PDEK"));
	
			long nombreSouduresDansPage = soudureRepository.countByPagePDEK(pagePDEK);
			int numeroCycle;
	
			if (nombreSouduresDansPage < 25) {
				numeroCycle = (int) nombreSouduresDansPage + 1;
			} else {
				pagePDEK = new PagePDEK(pdek.getTotalPages() + 1, false, pdek);
				pdekPageRepository.save(pagePDEK);
				pdek.setTotalPages(pdek.getTotalPages() + 1);
				pdekRepository.save(pdek);
				numeroCycle = 1;
			}
	
			instance1.setPdekSoudure(pdek);
			instance1.setPagePDEK(pagePDEK);
			instance1.setNumeroCycle(numeroCycle);
			soudureRepository.save(instance1);
	
			return new AjoutSoudureResultDTO(pdek.getId(), pagePDEK.getPageNumber());
	
		} else {
			// ⚙️ Cas nouveau PDEK
			PDEK newPDEK = new PDEK();
			newPDEK.setSectionFil(sectionFilSelectionner);
			newPDEK.setNombreEchantillons("5 Piéces ");
			newPDEK.setFrequenceControle(3100);
			newPDEK.setSegment(user.getSegment());
			newPDEK.setNumMachine(user.getMachine());
			newPDEK.setDateCreation(instanceSoudure.getDate());
			newPDEK.setTypeOperation(TypesOperation.Soudure);
			newPDEK.setPlant(user.getPlant());
			newPDEK.setUsersRempliePDEK(List.of(user));
			newPDEK.setTotalPages(1);
			pdekRepository.save(newPDEK);
	
			PagePDEK newPage = new PagePDEK(1, false, newPDEK);
			pdekPageRepository.save(newPage);
	
			Projet projetEntity = projetRepository.findByNom(projet)
					.orElseThrow(() -> new RuntimeException("Projet " + projet + " non trouvé"));
			newPDEK.getProjets().add(projetEntity);
			projetEntity.getPdeks().add(newPDEK);
			projetRepository.save(projetEntity);
	
			instance1.setPdekSoudure(newPDEK);
			instance1.setPagePDEK(newPage);
			instance1.setNumeroCycle(1);
			soudureRepository.save(instance1);
	
			return new AjoutSoudureResultDTO(newPDEK.getId(), newPage.getPageNumber());
		}
	}
	

	 @Override
	 public Map<Integer, List<SoudureDTO>> recupererSouduresParPDEKGroupéesParPage(String sectionFil, int segment, Plant plant, String nomProjet) {
	     Optional<PDEK> pdekExiste = pdekRepository.findUniquePDEK_SoudureUtrason(sectionFil, segment, plant, nomProjet);

	     if (pdekExiste.isPresent()) {
	         PDEK pdek = pdekExiste.get();
	         List<Soudure> soudures = soudureRepository.findByPdekSoudure_Id(pdek.getId());

	         // Grouper les soudures par numéro de page
	         return soudures.stream()
	                 .collect(Collectors.groupingBy(
	                         s -> s.getPagePDEK().getPageNumber(), // groupement par numéro de page
	                         Collectors.mapping(
	                                 s -> new SoudureDTO(
	                                         s.getId(),
	                                         s.getCode(),
	                                         s.getSectionFil(),
	                                         s.getDate().toString(),
	                                         s.getNumeroCycle(),
											 s.getUserSoudure().getMatricule(),
											 s.getMoyenne(),
											 s.getEtendu()),
	                                 Collectors.toList()
	                         )
	                 ));
	     } else {
	         return Map.of();
	     }
	 }


	 ///////
	 public int getLastNumeroCycle(String sectionFilSelectionne, int segment, Plant nomPlant, String projetName) {
		 // 1️⃣ Récupérer le PDEK correspondant
		 Optional<PDEK> pdekOpt = pdekRepository.findUniquePDEK_SoudureUtrason(sectionFilSelectionne, segment, nomPlant, projetName);
	 
		 if (pdekOpt.isEmpty()) {
			 // Aucun PDEK trouvé → retourner 0
			 return 0;
		 }
	 
		 PDEK pdek = pdekOpt.get();
	 
		 // 2️⃣ Récupérer la dernière page associée au PDEK
		 Optional<PagePDEK> lastPageOpt = pdekPageRepository.findFirstByPdekOrderByPageNumberDesc(pdek);
	 
		 if (lastPageOpt.isEmpty()) {
			 // Le PDEK existe, mais aucune page n'est encore créée → retourner 0
			 return 0;
		 }
	 
		 PagePDEK lastPage = lastPageOpt.get();
	 
		 // 3️⃣ Vérifier s'il existe des soudures dans cette page
		 long nombreSouduresDansPage = soudureRepository.countByPagePDEK(lastPage);
	 
		 if (nombreSouduresDansPage == 0) {
			 // Si la page est vide, retourner 0
			 return 0;
		 }
	 
		 // 4️⃣ Récupérer le dernier numéro de cycle
		 Optional<Soudure> lastSoudureOpt = soudureRepository.findTopByPagePDEK_IdOrderByNumeroCycleDesc(lastPage.getId());
	 
		 if (lastSoudureOpt.isPresent()) {
			 // Si une soudure est présente, retourner son numéro de cycle
			 return lastSoudureOpt.get().getNumeroCycle();
		 }
	 
		 // Si aucune soudure n'est trouvée malgré les vérifications, retourner 0
		 return 0;
	 }
	 /***************** Recuperation des soudure de page pdek actuel  **************/
	 

public List<SoudureDTO> recupererSouduresParPageActuel(String sectionFil, int segment, Plant plant, String nomProjet) {
    Optional<PDEK> pdekExiste = pdekRepository.findUniquePDEK_SoudureUtrason(sectionFil, segment, plant, nomProjet);

    if (pdekExiste.isPresent()) {
        PDEK pdek = pdekExiste.get();
        // Identifier la page PDEK actuelle (celle utilisée pour ajouter de nouvelles soudures)
        Optional<PagePDEK> pageActuelleOpt = pdekPageRepository.findPageActuelleByPdekId(pdek.getId());

        if (pageActuelleOpt.isPresent()) {
            int numeroPageActuelle = pageActuelleOpt.get().getPageNumber();

            List<Soudure> souduresPageActuelle = soudureRepository.findByPdekSoudure_IdAndPagePDEK_PageNumber(
                pdek.getId(),
                numeroPageActuelle
            );

            return souduresPageActuelle.stream()
                    .map(s -> new SoudureDTO(
                            s.getId(),
                            s.getCode(),
                            s.getSectionFil(),
                            s.getDate().toString(),
                            s.getNumeroCycle(),
                            s.getUserSoudure().getMatricule(),
							s.getMoyenne(),
							s.getEtendu() ))
                    .collect(Collectors.toList());
        }
    }

    return List.of(); // Si rien trouvé
}


	@Override
	public List<SoudureDTO> getSouduresNonValidees() {
		// TODO Auto-generated method stub
		return soudureRepository.findByDecision(0) ; 
	}


	@Override
	public List<SoudureDTO> getSouduresValidees() {
		// TODO Auto-generated method stub
		return soudureRepository.findByDecision(1);
	}
}