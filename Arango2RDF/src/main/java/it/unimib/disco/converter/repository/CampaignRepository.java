package it.unimib.disco.converter.repository;

import com.arangodb.springframework.repository.ArangoRepository;

import it.unimib.disco.converter.model.Campaign;

public interface CampaignRepository extends ArangoRepository<Campaign, String> {

}
