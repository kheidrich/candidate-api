package br.edu.ulbra.election.candidate.service;

import antlr.StringUtils;
import br.edu.ulbra.election.candidate.exception.GenericOutputException;
import br.edu.ulbra.election.candidate.input.v1.CandidateInput;
import br.edu.ulbra.election.candidate.model.Candidate;
import br.edu.ulbra.election.candidate.output.v1.CandidateOutput;
import br.edu.ulbra.election.candidate.output.v1.GenericOutput;
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

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_CANDIDATE_NOT_FOUND = "Candidate not found";
    private static final String MESSAGE_CANDIDATE_DELETED = "Candidate deleted";

    @Autowired
    public CandidateService(CandidateRepository candidateRepository, ModelMapper modelMapper) {
        this.candidateRepository = candidateRepository;
        this.modelMapper = modelMapper;
    }

    public List<CandidateOutput> getAll(){
        Type candidateOutputListType = new TypeToken<List<CandidateOutput>>(){}.getType();
        return modelMapper.map(candidateRepository.findAll(),candidateOutputListType);
    }

    public CandidateOutput create(CandidateInput candidateInput) {
        validateInput(candidateInput);
        Candidate candidate = modelMapper.map(candidateInput, Candidate.class);
        candidate = candidateRepository.save(candidate);
        return modelMapper.map(candidate, CandidateOutput.class);
    }

    public CandidateOutput getById(Long candidateId){
        if (candidateId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null){
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
        }

        return modelMapper.map(candidate, CandidateOutput.class);
    }

    public CandidateOutput update(Long candidateId, CandidateInput candidateInput) {
        if (candidateId == null)
            throw new GenericOutputException(MESSAGE_INVALID_ID);

        validateInput(candidateInput);

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null)
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);

        candidateInput.setElectionId(candidateInput.getElectionId());
        candidateInput.setName(candidateInput.getName());
        candidateInput.setNumberElection(candidateInput.getNumberElection());
        candidateInput.setPartyId(candidateInput.getPartyId());
        candidate = candidateRepository.save(candidate);

        return modelMapper.map(candidate, CandidateOutput.class);
    }

    public GenericOutput delete(Long candidateId) {
        if (candidateId == null)
            throw new GenericOutputException(MESSAGE_INVALID_ID);

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null)
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);

        candidateRepository.delete(candidate);

        return new GenericOutput(MESSAGE_CANDIDATE_DELETED);
    }

    private void validateInput(CandidateInput input) {
        if (input.getElectionId() != null && input.getElectionId() < 0)
            throw new GenericOutputException("Invalid electionId");

        if (input.getName() != null && input.getName().isEmpty())
            throw new GenericOutputException("Invalid name");

        if (input.getNumberElection() != null && input.getNumberElection() < 0)
            throw new GenericOutputException("Invalid number election");

        if (input.getPartyId() != null && input.getPartyId() < 0)
            throw new GenericOutputException("Invalid partyId");
    }

}
