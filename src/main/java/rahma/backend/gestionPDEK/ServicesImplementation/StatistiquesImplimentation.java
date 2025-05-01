package rahma.backend.gestionPDEK.ServicesImplementation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rahma.backend.gestionPDEK.DTO.OperateurErreurDTO;
import rahma.backend.gestionPDEK.Entity.TypesOperation;
import rahma.backend.gestionPDEK.Entity.User;
import rahma.backend.gestionPDEK.Repository.PdekRepository;
import rahma.backend.gestionPDEK.Repository.PlanActionRepository;
import rahma.backend.gestionPDEK.Repository.SertissageIDCRepository;
import rahma.backend.gestionPDEK.Repository.SertissageNormalRepository;
import rahma.backend.gestionPDEK.Repository.SoudureRepository;
import rahma.backend.gestionPDEK.Repository.TorsadageRepository;
import rahma.backend.gestionPDEK.Repository.UserRepository;
import rahma.backend.gestionPDEK.ServicesInterfaces.StatistiquesService;

@Service
public class StatistiquesImplimentation implements StatistiquesService {


	 @Autowired    private UserRepository userRepository;	
	 @Autowired    private SertissageNormalRepository sertissageNormalRepository;	
	 @Autowired    private SertissageIDCRepository sertissageIDCRepository;	
	 @Autowired    private SoudureRepository soudureRepository;	
	 @Autowired    private TorsadageRepository torsadageRepository;	
	 @Autowired    private PdekRepository pdekRepository;	
	 @Autowired    private PlanActionRepository planActionRepository;	
	
	@Override
	public long nombreTotalOperateurs() {
	    List<String> rolesOperateurs = List.of("OPERATEUR", "CHEF_DE_LIGNE", "AGENT_QUALITE");
	    return userRepository.countByRoleNomIn(rolesOperateurs);
	}


	@Override
	public long nombreHommesOperateurs() {
	    List<String> rolesOperateurs = List.of("OPERATEUR", "CHEF_DE_LIGNE", "AGENT_QUALITE");
	    return userRepository.countByRoleNomInAndSexe(rolesOperateurs , "homme");
	}


	@Override
	public long nombreFemmesOperateurs() {
	    List<String> rolesOperateurs = List.of("OPERATEUR", "CHEF_DE_LIGNE", "AGENT_QUALITE");
	    return userRepository.countByRoleNomInAndSexe(rolesOperateurs , "femme");
	}

	@Override
	public double calculerPourcentageAugmentationOperateurs() {
	    List<String> rolesOperateurs = List.of("OPERATEUR", "CHEF_DE_LIGNE", "AGENT_QUALITE");

	    String anneeActuelle = String.valueOf(LocalDate.now().getYear());
	    String anneePrecedente = String.valueOf(LocalDate.now().getYear() - 1);

	    long totalAnneePrecedente = userRepository.compterParRoleEtAnnee(rolesOperateurs, anneePrecedente);
	    long totalAnneeActuelle = userRepository.compterParRoleEtAnnee(rolesOperateurs, anneeActuelle);

	    if (totalAnneePrecedente == 0) {
	        return totalAnneeActuelle > 0 ? 100.0 : 0.0;
	    }

	    return ((double)(totalAnneeActuelle - totalAnneePrecedente) / totalAnneePrecedente) * 100.0;
	}

/************************ erreurs process ******************************************/
	@Override
	public long nombreErreursSertissageNormalCetteSemaine() {
		  // 1) Calcul des bornes de la semaine
        LocalDate today = LocalDate.now();
        LocalDate lundi = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // 2) Formatage en String « yyyy-MM-dd »
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = lundi.format(fmt);
        String endDate   = today.format(fmt);

        // 3) Compte des erreurs en fonction de la zone (non nulle)
        return sertissageNormalRepository.countByDateBetweenAndZoneNotNull(startDate, endDate);
    }


@Override
public long nombreErreursSertissageIDCCetteSemaine() {
	// 1) Calcul des bornes de la semaine
    LocalDate today = LocalDate.now();
    LocalDate lundi = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    // 2) Formatage en String « yyyy-MM-dd »
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String startDate = lundi.format(fmt);
    String endDate   = today.format(fmt);

    // 3) Compte des erreurs en fonction de la zone (non nulle)
    return sertissageIDCRepository.countByDateBetweenAndZoneNotNull(startDate, endDate);
}


@Override
public long nombreErreursSoudureCetteSemaine() {
	// 1) Calcul des bornes de la semaine
    LocalDate today = LocalDate.now();
    LocalDate lundi = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    // 2) Formatage en String « yyyy-MM-dd »
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String startDate = lundi.format(fmt);
    String endDate   = today.format(fmt);

    // 3) Compte des erreurs en fonction de la zone (non nulle)
    return soudureRepository.countByDateBetweenAndZoneNotNull(startDate, endDate);
}


@Override
public long nombreErreursTorsadgeCetteSemaine() {
	// 1) Calcul des bornes de la semaine
    LocalDate today = LocalDate.now();
    LocalDate lundi = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    // 2) Formatage en String « yyyy-MM-dd »
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String startDate = lundi.format(fmt);
    String endDate   = today.format(fmt);

    // 3) Compte des erreurs en fonction de la zone (non nulle)
    return torsadageRepository.countByDateBetweenAndZoneNotNull(startDate, endDate);
	
}


@Override
public long nombreErreursTotalCetteSemaineSaufPistolet() {
	
	long erreursSertissagesNormal = nombreErreursSertissageNormalCetteSemaine() ; 
	long erreursSertissagesIDC = nombreErreursSertissageIDCCetteSemaine() ; 
	long erreursSoudure = nombreErreursSoudureCetteSemaine() ;  
	long erreursTorsadage = nombreErreursTorsadgeCetteSemaine() ; 
	return erreursSertissagesNormal + erreursSertissagesIDC + erreursSoudure + erreursTorsadage;
}

@Override
public double calculerPourcentageSemainePrecdant() {
    LocalDate today = LocalDate.now();
    LocalDate debutSemaineCourante = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate finSemaineCourante = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    LocalDate debutSemainePrecedente = debutSemaineCourante.minusWeeks(1);
    LocalDate finSemainePrecedente = debutSemaineCourante.minusDays(1);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String startCurrent = debutSemaineCourante.format(formatter);
    String endCurrent = finSemaineCourante.format(formatter);
    String startPrev = debutSemainePrecedente.format(formatter);
    String endPrev = finSemainePrecedente.format(formatter);

    long erreursCurrent =
        sertissageNormalRepository.countByDateBetweenAndZoneNotNull(startCurrent, endCurrent)
      + sertissageIDCRepository.countByDateBetweenAndZoneNotNull(startCurrent, endCurrent)
      + soudureRepository.countByDateBetweenAndZoneNotNull(startCurrent, endCurrent)
      + torsadageRepository.countByDateBetweenAndZoneNotNull(startCurrent, endCurrent);

    long erreursPrev =
        sertissageNormalRepository.countByDateBetweenAndZoneNotNull(startPrev, endPrev)
      + sertissageIDCRepository.countByDateBetweenAndZoneNotNull(startPrev, endPrev)
      + soudureRepository.countByDateBetweenAndZoneNotNull(startPrev, endPrev)
      + torsadageRepository.countByDateBetweenAndZoneNotNull(startPrev, endPrev);

    if (erreursPrev == 0) {
        return erreursCurrent == 0 ? 0.0 : erreursCurrent; // retourne le nombre brut d'erreurs comme "indice"
    }

    double difference = erreursCurrent - erreursPrev;
    return (difference / (double) erreursPrev) * 100.0;
}


@Override
public Map<String, Long> getNombrePdekParTypeOperation() {
    String currentYear = String.valueOf(LocalDate.now().getYear());
    List<Object[]> results = pdekRepository.countPdekByTypeOperationForYear(currentYear);
    Map<String, Long> map = new HashMap<>();
    for (Object[] row : results) {
        String type = (String) row[0]; // type_operation est maintenant une String si requête native
        Long count = ((Number) row[1]).longValue(); // sécurité pour éviter ClassCastException
        map.put(type, count);
    }
    return map;
}


@Override
public Map<String, Long> getNombrePlanActionParTypeOperation() {
	  String currentYear = String.valueOf(LocalDate.now().getYear());
	    List<Object[]> results = planActionRepository.countPlanActionByTypeOperationForYear(currentYear);
	    Map<String, Long> map = new HashMap<>();
	    for (Object[] row : results) {
	        String type = (String) row[0]; // type_operation est maintenant une String si requête native
	        Long count = ((Number) row[1]).longValue(); // sécurité pour éviter ClassCastException
	        map.put(type, count);
	    }
	    return map;
	}


@Override
public List<OperateurErreurDTO> getTop5OperateursWithErrors() {
    List<Object[]> results = sertissageIDCRepository.findTop5OperateursWithErrors();

    List<OperateurErreurDTO> dtos = new ArrayList<>();

    for (Object[] result : results) {
        User user = (User) result[0];
        long nombreErreurs = (long) result[1];
        
        OperateurErreurDTO dto = new OperateurErreurDTO(
            user.getMatricule(),
            user.getPrenom()+" "+user.getNom() ,
            user.getPoste(),
            user.getMachine(),
            user.getRole().getNom(), 
            user.getPlant().toString(),
            user.getSegment(),
            user.getTypeOperation().toString(),
            nombreErreurs  );
      
        dtos.add(dto);
    }

    return dtos;
}



}
