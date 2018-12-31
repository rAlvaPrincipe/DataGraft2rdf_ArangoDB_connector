package it.unimib.disco.converter.model;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Field;
import com.arangodb.springframework.annotation.Relations;

@Document(value = "`JOT-campaigns-spain`")
public class Campaign {

	@Id
	private String id;

	@Field("dbp:date")
	private String date;

	@Field("google:adPosition")
	private String adPosition;

	@Field("google:matchType")
	private String matchType;

	@Field("google:numberOfClicks")
	private String numberOfClicks;

	@Field("google:numberOfImpressions")
	private String numberOfImpressions;

	@Field("jot:belongsToCategoryName")
	private String belongsToCategoryName;

	@Field("jot:belongsToRegionId")
	private String belongsToRegionId;

	@Field("jot:inCityName")
	private String inCityName;

	@Field("jot:inCountryCode")
	private String inCountryCode;

	private String label;
	private String prefix;
	private String rdf;
	private String[] type;
	private String value;

	@Relations(edges = EdgeCampaign.class, lazy = true)
	private Collection<Campaign> relation;

	public Collection<Campaign> getRelation() {
		return relation;
	}

	public void setRelation(Collection<Campaign> relation) {
		this.relation = relation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAdPosition() {
		return adPosition;
	}

	public void setAdPosition(String adPosition) {
		this.adPosition = adPosition;
	}

	public String getMatchType() {
		return matchType;
	}

	public void setMatchType(String matchType) {
		this.matchType = matchType;
	}

	public String getNumberOfClicks() {
		return numberOfClicks;
	}

	public void setNumberOfClicks(String numberOfClicks) {
		this.numberOfClicks = numberOfClicks;
	}

	public String getNumberOfImpressions() {
		return numberOfImpressions;
	}

	public void setNumberOfImpressions(String numberOfImpressions) {
		this.numberOfImpressions = numberOfImpressions;
	}

	public String getBelongsToCategoryName() {
		return belongsToCategoryName;
	}

	public void setBelongsToCategoryName(String belongsToCategoryName) {
		this.belongsToCategoryName = belongsToCategoryName;
	}

	public String getBelongsToRegionId() {
		return belongsToRegionId;
	}

	public void setBelongsToRegionId(String belongsToRegionId) {
		this.belongsToRegionId = belongsToRegionId;
	}

	public String getInCityName() {
		return inCityName;
	}

	public void setInCityName(String inCityName) {
		this.inCityName = inCityName;
	}

	public String getInCountryCode() {
		return inCountryCode;
	}

	public void setInCountryCode(String inCountryCode) {
		this.inCountryCode = inCountryCode;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getRdf() {
		return rdf;
	}

	public void setRdf(String rdf) {
		this.rdf = rdf;
	}

	public String[] getType() {
		return type;
	}

	public void setType(String[] type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Campaign [id=" + id + ", rdf= " + this.rdf + "]";
	}
}
