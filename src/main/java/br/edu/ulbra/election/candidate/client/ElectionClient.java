package br.edu.ulbra.election.candidate.client;

import br.edu.ulbra.election.candidate.output.v1.ElectionOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "election-service", url = "${url.election-service}")
public interface ElectionClient {

    @GetMapping("/v1/election/{electionId}")
    ElectionOutput getById(@PathVariable(name = "electionId") Long electionId);

    @GetMapping("/v1/vote/{electionId}")
    List<ElectionOutput> getByElectionId(@PathVariable(name = "electionId") Long electionId);

}
