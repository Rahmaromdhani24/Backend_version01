package rahma.backend.gestionPDEK.PDF;

import rahma.backend.gestionPDEK.ServicesPDF.ServicePistoletJaune;
import rahma.backend.gestionPDEK.DTO.PistoletDTO;
import rahma.backend.gestionPDEK.Entity.PDEK;
import rahma.backend.gestionPDEK.Entity.TypePistolet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/pdf")
public class PistoletJaune {
	
	 private final ServicePistoletJaune pistoletPdfService;

	    public PistoletJaune(ServicePistoletJaune pistoletPdfService) {
	        this.pistoletPdfService = pistoletPdfService;
	    }

	    @GetMapping("/pistoletJaune")
	    public ResponseEntity<byte[]> generatePdf() {
	        try {
	            byte[] pdfContent = pistoletPdfService.generatePdf();
	            
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_PDF);
	            // Option 1: Pour afficher dans le navigateur
	            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=pistolet_jaune.pdf");
	            // Option 2: Pour forcer le téléchargement
	            // headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pistolet_jaune.pdf");
	            
	            return ResponseEntity.ok()
	                    .headers(headers)
	                    .body(pdfContent);
	                    
	        } catch (IOException e) {
	            // Log l'erreur
	            // logger.error("Erreur lors de la génération du PDF", e);
	            
	            return ResponseEntity.internalServerError()
	                    .body(("Erreur lors de la génération du PDF: " + e.getMessage()).getBytes());
	        }
	    }
	}