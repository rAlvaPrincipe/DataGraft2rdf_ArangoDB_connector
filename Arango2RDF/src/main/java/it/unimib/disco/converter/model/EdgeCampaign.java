package it.unimib.disco.converter.model;

import org.springframework.data.annotation.Id;

import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.annotation.From;
import com.arangodb.springframework.annotation.To;

@Edge(value = "`JOT-campaigns-spain-edge`")
public class EdgeCampaign {

	@Id
	private String id;

	@From
	private Campaign child;

	@To
	private Campaign parent;

	private String rdf;

	public EdgeCampaign(final Campaign child, final Campaign parent) {
		super();
		this.child = child;
		this.parent = parent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Campaign getChild() {
		return child;
	}

	public void setChild(Campaign child) {
		this.child = child;
	}

	public Campaign getParent() {
		return parent;
	}

	public void setParent(Campaign parent) {
		this.parent = parent;
	}

	public String getRdf() {
		return rdf;
	}

	public void setRdf(String rdf) {
		this.rdf = rdf;
	}

	@Override
	public String toString() {
		return "Edge [id=" + id + ", rdf=" + rdf + ", child=" + child + ", parent=" + parent + "]";
	}
}
