package com.unievt;

import com.unievt.event.entity.Intervenant;
import com.unievt.event.repository.IntervenantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UniEventBackendApplicationTests {

    @Autowired
    private IntervenantRepository intervenantRepository;

    @Test
    void testSaveIntervenant() {
        Intervenant i = Intervenant.builder()
                .nom("John Doe")
                .institution("FST Settat")
                .biographie("Expert en IA")
                .build();

        Intervenant saved = intervenantRepository.save(i);

        assertNotNull(saved.getId());
        System.out.println("Saved with id: " + saved.getId());
    }
}