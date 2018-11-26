package br.edu.ulbra.election.candidate.client;

import br.edu.ulbra.election.candidate.output.v1.PartyOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "party-service", url = "${url.party-service}")
public interface PartyClient {

    @GetMapping("/v1/party/{partyId}")
    PartyOutput getById (@PathVariable(name = "partyId") Long partyId);

    @GetMapping("/v1/candidate/party/{partyId}")
    List<PartyOutput> getByPartyId(@PathVariable(name = "partyId") Long partyId);
    
    @GetMapping("/v1/candidate/party/")
    List<PartyOutput> findAll();
}
