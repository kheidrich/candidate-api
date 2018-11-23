package br.edu.ulbra.election.candidate.service;

import br.edu.ulbra.election.candidate.client.ElectionClient;
import br.edu.ulbra.election.candidate.output.v1.ElectionOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElectionClientService {
    private final ElectionClient electionClient;

    @Autowired
    public ElectionClientService(ElectionClient electionClient){
        this.electionClient = electionClient;
    }

    public List<ElectionOutput> getElectionId(Long electionId){
        return this.electionClient.getByElectionId(electionId);
    }
}
