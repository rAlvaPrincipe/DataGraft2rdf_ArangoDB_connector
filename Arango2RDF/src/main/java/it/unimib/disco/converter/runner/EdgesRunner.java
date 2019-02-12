package it.unimib.disco.converter.runner;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;

import it.unimib.disco.converter.Filler;
import it.unimib.disco.converter.model.EdgeCampaign;
import it.unimib.disco.converter.repository.EdgeCampaignRepository;

@ComponentScan("it.unimib.disco.converter")
public class EdgesRunner extends Filler implements CommandLineRunner {
	
	@Autowired
	private EdgeCampaignRepository edgeRepo;
	private File output_f;

	public void init_ontology() {
		model = ModelFactory.createDefaultModel();
	}
	
	@Override
	public void run(String... args) throws Exception {
		initialize(args[5], Integer.valueOf(args[1]), 188486004, "edges");
		output_f = new File(args[3]);
		readLog();
		find_holes();
	}

	
	public void request( int nPage, int pageSize) throws Exception {
		try {
			PageRequest request =   PageRequest.of(nPage, pageSize);
			Page<EdgeCampaign> page = edgeRepo.findAll(request);
			System.out.print(". Processing the response");
			System.out.println(page);
			for (EdgeCampaign el : page) {
				Resource source = model.createResource(el.getChild().getRdf());
				Resource dest = model.createResource(el.getParent().getRdf());
				Property prop = model.createProperty(el.getRdf());
				source.addProperty(prop, dest);
			}
			FileOutputStream fos = new FileOutputStream(output_f, true);
			model.write(fos, "N-TRIPLES");
			fos.close();
			System.out.print(". RDF writted");
		}
		catch(Exception e) {
			System.out.print(". Refused");
			throw e;
		}

	}
}
