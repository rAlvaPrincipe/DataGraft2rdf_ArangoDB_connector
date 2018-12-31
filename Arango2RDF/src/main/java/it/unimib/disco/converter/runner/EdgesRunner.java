package it.unimib.disco.converter.runner;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Property;

import it.unimib.disco.converter.model.EdgeCampaign;
import it.unimib.disco.converter.repository.EdgeCampaignRepository;

@ComponentScan("it.unimib.disco.converter")
public class EdgesRunner implements CommandLineRunner {

	@Autowired
	private EdgeCampaignRepository edgeRepo;

	private Model model;
	private File output_f;

	@Override
	public void run(String... args) throws Exception {
		output_f = new File(args[3]);
		rdfize(Integer.valueOf(args[1]));
	}
	
	public void init() {
		model = ModelFactory.createDefaultModel();
	}
	
	public void rdfize(int pageSize) throws Exception {
		long count = edgeRepo.count();
		System.out.println("count: " + count);
		int nPage = 0;
		while (nPage < Math.ceil((float) count / (float) pageSize)) {
			init();
			try {
				request(nPage, pageSize);
			} catch (Exception e) {
				e.printStackTrace();
			}
			nPage++;
		}
		System.out.println("done!");
	}
	
	public void request( int nPage, int pageSize) throws Exception {
		System.out.println("processing request edge page: " + nPage + " pageSize: " + pageSize);
		PageRequest request =   PageRequest.of(nPage, pageSize);
		Page<EdgeCampaign> page = edgeRepo.findAll(request);
		System.out.println("size: " + page.getSize());
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
	}
}
