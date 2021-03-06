package br.edu.ulbra.election.candidate.api.v1;

import br.edu.ulbra.election.candidate.input.v1.CandidateInput;
import br.edu.ulbra.election.candidate.output.v1.CandidateOutput;
import br.edu.ulbra.election.candidate.output.v1.GenericOutput;
import br.edu.ulbra.election.candidate.service.CandidateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/candidate")
public class CandidateApi {

    private CandidateService candidateService;

        public CandidateApi(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping("/")
    @ApiOperation(value = "Get candidates List")
    public List<CandidateOutput> getAll(){
        return candidateService.getAll();
    }

    @GetMapping("/{candidateId}")
    @ApiOperation(value = "Get candidate by Id")
    public CandidateOutput getById(@PathVariable Long candidateId){
        return candidateService.getById(candidateId);
    }

    @GetMapping("/election/{electionId}")
    @ApiOperation(value = "Get candidate by election Id")
    public List<CandidateOutput> getByElectionId(@PathVariable Long electionId){
        return this.candidateService.getByElectionId(electionId);
    }

    @GetMapping("/party/{partyId}")
    @ApiOperation(value = "Get candidate by party Id")
    public List<CandidateOutput> getByPartyId(@PathVariable Long partyId){
        return this.candidateService.getByPartyId(partyId);
    }

    @PostMapping("/")
    @ApiOperation(value = "Create new candidate")
    public CandidateOutput create(@RequestBody CandidateInput candidateInput){
        return candidateService.create(candidateInput);
    }

    @PutMapping("/{candidateId}")
    @ApiOperation(value = "Update candidate")
    public CandidateOutput update(@PathVariable Long candidateId, @RequestBody CandidateInput candidateInput){
        return candidateService.update(candidateId,candidateInput);
    }

    @DeleteMapping("/{candidateId}")
    @ApiOperation(value = "Delete candidate")
    public GenericOutput delete(@PathVariable Long candidateId){
        return candidateService.delete(candidateId);
    }
}
