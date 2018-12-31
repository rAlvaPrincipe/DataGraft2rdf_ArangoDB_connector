package it.unimib.disco.converter.repository;

import com.arangodb.springframework.repository.ArangoRepository;

import it.unimib.disco.converter.model.EdgeCampaign;

public interface EdgeCampaignRepository extends ArangoRepository<EdgeCampaign, String> {

}
