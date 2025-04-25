package com.example.Userservice.Controller;
import com.example.Userservice.DTO.ApiResponse;
import com.example.Userservice.DTO.SkillDTO;
import com.example.Userservice.Service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    
    @GetMapping
    @PreAuthorize("hasAnyRole('MENTOR', 'ADMIN', 'STUDENT')")
    public ResponseEntity<ApiResponse<?>> getSkills(
            @RequestParam(value = "id", required = false) UUID id,
            @RequestParam(value = "name", required = false) String name) {
        
  
        if (id != null) {
            SkillDTO skill = skillService.getSkillById(id);
            if (skill != null) {
                return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Skill found", skill));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Skill not found", null));
            }
        }

      
        List<SkillDTO> skills = skillService.getAllSkills();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "List of skills", skills));
    }

    
    @PostMapping
    public ResponseEntity<SkillDTO> createOrUpdateSkill(@RequestBody SkillDTO skillDTO,
                                                        @RequestParam(required = false) UUID id) {
        return ResponseEntity.ok(skillService.createOrUpdateSkill(id, skillDTO));
    }

   
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSkill(@PathVariable UUID id) {
    skillService.deleteSkill(id);
    Map<String, String> response = new HashMap<>();
    response.put("message", "Skill deleted successfully");
    return ResponseEntity.ok(response);
}

}
