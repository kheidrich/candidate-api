package br.edu.ulbra.election.candidate.service;

import br.edu.ulbra.election.candidate.client.PartyClient;
import br.edu.ulbra.election.candidate.output.v1.PartyOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyClientService {
    private final PartyClient partyClient;

    @Autowired
    public PartyClientService(PartyClient partyClient){

        this.partyClient = partyClient;
    }

    public PartyOutput getById(Long partyId){
        return this.partyClient.getById(partyId);
    }


    public List<PartyOutput> getByPartyId(Long partyId){
        return this.partyClient.getByPartyId(partyId);
    }
}
