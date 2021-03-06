package br.edu.ulbra.election.candidate.service;

import br.edu.ulbra.election.candidate.exception.GenericOutputException;
import br.edu.ulbra.election.candidate.input.v1.CandidateInput;
import br.edu.ulbra.election.candidate.model.Candidate;
import br.edu.ulbra.election.candidate.output.v1.CandidateOutput;
import br.edu.ulbra.election.candidate.output.v1.ElectionOutput;
import br.edu.ulbra.election.candidate.output.v1.GenericOutput;
import br.edu.ulbra.election.candidate.output.v1.PartyOutput;
import br.edu.ulbra.election.candidate.repository.CandidateRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final ModelMapper modelMapper;
    private final PartyClientService partyClientService;
    private final ElectionClientService electionClientService;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_CANDIDATE_NOT_FOUND = "Candidate not found";
    private static final String MESSAGE_CANDIDATE_DELETED = "Candidate deleted";

    @Autowired
    public CandidateService(CandidateRepository candidateRepository, ModelMapper modelMapper,PartyClientService partyClientService,ElectionClientService electionClientService) {
        this.candidateRepository = candidateRepository;
        this.modelMapper = modelMapper;
        this.partyClientService = partyClientService;
        this.electionClientService = electionClientService;
    }

    public List<CandidateOutput> getAll() {
        Type candidateOutputListType = new TypeToken<List<CandidateOutput>>() {
        }.getType();
        return modelMapper.map(candidateRepository.findAll(), candidateOutputListType);
    }

    public CandidateOutput create(CandidateInput candidateInput) {
        validateInput(candidateInput);
        Candidate candidate = modelMapper.map(candidateInput, Candidate.class);
        candidate = candidateRepository.save(candidate);
        return modelMapper.map(candidate, CandidateOutput.class);
    }

    public CandidateOutput getById(Long candidateId) {
        if (candidateId == null) {
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null) {
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
        }

        return modelMapper.map(candidate, CandidateOutput.class);
    }

    public List<CandidateOutput> getByElectionId(Long electionId) {
        List<CandidateOutput> candidates = new ArrayList<>();

        for (Candidate c : this.candidateRepository.findAll())
            if (c.getElectionId().equals(electionId))
                candidates.add(modelMapper.map(c, CandidateOutput.class));

        return candidates;
    }

    public List<CandidateOutput> getByPartyId(Long partyId) {
        List<CandidateOutput> candidates = new ArrayList<>();

        for (Candidate c : this.candidateRepository.findAll())
            if (c.getElectionId().equals(partyId))
                candidates.add(modelMapper.map(c, CandidateOutput.class));

        return candidates;
    }

    public CandidateOutput update(Long candidateId, CandidateInput candidateInput) {
        if (candidateId == null)
            throw new GenericOutputException(MESSAGE_INVALID_ID);

        validateInput(candidateInput);

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null)
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);

        if (candidateHasVote(candidateId)){
            throw new GenericOutputException("Candidate has vote");
        }else {
            candidateInput.setElectionId(candidateInput.getElectionId());
            candidateInput.setName(candidateInput.getName());
            candidateInput.setNumberElection(candidateInput.getNumberElection());
            candidateInput.setPartyId(candidateInput.getPartyId());
            candidate = candidateRepository.save(candidate);

            return modelMapper.map(candidate, CandidateOutput.class);
        }
    }

    public GenericOutput delete(Long candidateId) {
        if (candidateId == null)
            throw new GenericOutputException(MESSAGE_INVALID_ID);

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null)
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);

        if(!candidateHasVote(candidateId)) {

            candidateRepository.delete(candidate);
            return new GenericOutput(MESSAGE_CANDIDATE_DELETED);
        }else {
            throw new GenericOutputException("Candidate has vote");
        }
    }

    private void validateInput(CandidateInput input) {
        if (input.getElectionId() == null || input.getElectionId() < 0 || electionExists(input.getElectionId()))
            throw new GenericOutputException("Invalid electionId");

        if (input.getName() == null || input.getName().length() < 5 || input.getName().split(" ").length < 2)
            throw new GenericOutputException("Invalid name");

        if (input.getNumberElection() == null || input.getNumberElection() < 0 || validateNumber(Integer.parseInt(input.getNumberElection().toString().substring(0, 2))))
            throw new GenericOutputException("Invalid number election");

        if (input.getPartyId() == null || input.getPartyId() < 0 || partyExists(input.getPartyId()))
            throw new GenericOutputException("Invalid partyId");
    }

    private boolean partyExists(Long partyId){
        List<PartyOutput> parties = this.partyClientService.getByPartyId(partyId);

        return parties.size() > 0;
    }

    private boolean electionExists(Long electionId){
        List<ElectionOutput> elections = this.electionClientService.getId(electionId);

        return elections.size() > 0;
    }
    
    public boolean validateNumber(Integer number) {
        Type partyOutputListType = new TypeToken<List<PartyOutput>>() {
        }.getType();

        List<PartyOutput> parties = modelMapper.map(partyClientService.findAll(), partyOutputListType);
        for (PartyOutput party : parties)
            if (party.getNumber().equals(number))
                return false;

        return true;
    }

    public boolean candidateHasVote(Long electionId){
        List<ElectionOutput> elections = this.electionClientService.getElectionId(electionId);

        return elections.size() > 0;
    }
}
