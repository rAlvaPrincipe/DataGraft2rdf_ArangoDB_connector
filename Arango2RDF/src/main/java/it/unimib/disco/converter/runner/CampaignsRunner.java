package it.unimib.disco.converter.runner;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

import it.unimib.disco.converter.model.Campaign;
import it.unimib.disco.converter.repository.CampaignRepository;

@ComponentScan("it.unimib.disco.converter")
public class CampaignsRunner implements CommandLineRunner{
	
	@Autowired
	private CampaignRepository campaignRepo;

	private Model model;
	private Property date;
	private Property adPosition;
	private Property matchType;
	private Property numberOfClicks;
	private Property numberOfImpressions;
	private Property belongsToCategoryName;
	private Property belongsToRegionId;
	private Property inCityName;
	private Property inCountryCode;
	private Property label;
	private Property type;
	
	private File output_f;
	
	
	public void run(String... args) throws Exception {
		output_f = new File(args[2]);
		rdfize(Integer.valueOf(args[0]));
	}

	public void init() {
		model = ModelFactory.createDefaultModel();
		date = model.createProperty("http://dbpedia.org/ontology/date");
		adPosition = model.createProperty("https://www.google.com/rdf#adPosition");
		matchType = model.createProperty("https://www.google.com/rdf#matchType");
		numberOfClicks = model.createProperty("https://www.google.com/rdf#numberOfClicks");
		numberOfImpressions = model.createProperty("https://www.google.com/rdf#numberOfImpressions");
		belongsToCategoryName = model.createProperty("http://jot-im.com/rdf-example#belongsToCategoryName");
		belongsToRegionId = model.createProperty("http://jot-im.com/rdf-example#beliongsToRegionId");
		inCityName = model.createProperty("http://jot-im.com/rdf-example#inCityName");
		inCountryCode = model.createProperty("http://jot-im.com/rdf-example#inCountryCode");
		label = model.createProperty("http://www.w3.org/2000/01/rdf-schema#label");
		type = model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
	}
	
	public void rdfize(int pageSize) throws Exception{
		long count = campaignRepo.count();
		System.out.println("count: " + count);
		int nPage = 0;
		while(nPage < Math.ceil((float)count/(float)pageSize)) {
			init();
			try{request(nPage, pageSize);}
			catch(Exception e) {e.printStackTrace();}
			nPage ++;
		}
		System.out.println("done!");
	}
	
	public void request( int nPage, int pageSize) throws Exception {
		System.out.println("processing request page: " + nPage + " pageSize: " + pageSize);
		PageRequest request =  PageRequest.of(nPage, pageSize);
		Page<Campaign> page = campaignRepo.findAll(request);
		System.out.println("size: " + page.getSize());
		System.out.println(page);
		for (Campaign el : page) {
			Resource subj = model.createResource(el.getRdf());
			if (el.getDate() != null) {
				Literal dateLit = ResourceFactory.createTypedLiteral(String.valueOf(el.getDate()), XSDDatatype.XSDdateTime);
				subj.addLiteral(date, dateLit);
			}
			if (el.getAdPosition() != null)
				subj.addProperty(adPosition, el.getAdPosition());
			if (el.getMatchType() != null)
				subj.addProperty(matchType, el.getMatchType());
			if (el.getNumberOfClicks() != null)
				subj.addProperty(numberOfClicks, el.getNumberOfClicks());
			if (el.getNumberOfImpressions() != null)
				subj.addProperty(numberOfImpressions, el.getNumberOfImpressions());
			if (el.getBelongsToCategoryName() != null)
				subj.addProperty(belongsToCategoryName, el.getBelongsToCategoryName());
			if (el.getBelongsToRegionId() != null)
				subj.addProperty(belongsToRegionId, el.getBelongsToRegionId());
			if (el.getInCityName() != null)
				subj.addProperty(inCityName, el.getInCityName());
			if (el.getInCountryCode() != null)
				subj.addProperty(inCountryCode, el.getInCountryCode());
			if (el.getLabel() != null)
				subj.addProperty(label, el.getLabel());
			for(String typo : el.getType()) {
				Resource typoC = model.createResource(typo);
				subj.addProperty(type, typoC);
			}
		}
		FileOutputStream fos = new FileOutputStream(output_f, true);
		model.write(fos, "N-TRIPLES");
		fos.close();
	}
}
