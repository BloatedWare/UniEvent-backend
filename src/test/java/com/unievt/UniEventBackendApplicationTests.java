package com.unievt;

import com.unievt.club.entity.Club;
import com.unievt.club.repository.ClubRepository;
import com.unievt.enums.RoleEnum;
import com.unievt.event.entity.Intervenant;
import com.unievt.event.repository.IntervenantRepository;
import com.unievt.user.entity.Etudiant;
import com.unievt.user.entity.Utilisateur;
import com.unievt.user.repository.EtudiantRepository;
import com.unievt.user.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UniEventBackendApplicationTests {

    @Autowired private UtilisateurRepository utilisateurRepository;
    @Autowired private EtudiantRepository etudiantRepository;
    @Autowired private ClubRepository clubRepository;
    @Autowired private IntervenantRepository intervenantRepository;

    // ─────────────────────────────────────────────
    //  UTILISATEUR
    // ─────────────────────────────────────────────

    @Test
    void testSaveAdmin() {
        Utilisateur admin = Utilisateur.builder()
                .nom("Bagri").prenom("Ikram")
                .email("ikram.bagri@unievt.ma")
                .motDePasse("admin123")
                .role(RoleEnum.ADMIN).actif(true)
                .build();

        Utilisateur saved = utilisateurRepository.save(admin);
        assertNotNull(saved.getId());
        assertEquals("Bagri", saved.getNom());
        System.out.println("✅ Admin saved: " + saved.getId());
    }

    @Test
    void testSaveDoyen() {
        Utilisateur doyen = Utilisateur.builder()
                .nom("Alami").prenom("Hassan")
                .email("hassan.alami@unievt.ma")
                .motDePasse("doyen123")
                .role(RoleEnum.DOYEN).actif(true)
                .build();

        Utilisateur saved = utilisateurRepository.save(doyen);
        assertNotNull(saved.getId());
        assertEquals(RoleEnum.DOYEN, saved.getRole());
        System.out.println("✅ Doyen saved: " + saved.getId());
    }

    @Test
    void testSaveResponsableEvenements() {
        Utilisateur responsable = Utilisateur.builder()
                .nom("Cherkaoui").prenom("Nadia")
                .email("nadia.cherkaoui@unievt.ma")
                .motDePasse("resp123")
                .role(RoleEnum.RESPONSABLE_EVENEMENTS).actif(true)
                .build();

        Utilisateur saved = utilisateurRepository.save(responsable);
        assertNotNull(saved.getId());
        assertEquals(RoleEnum.RESPONSABLE_EVENEMENTS, saved.getRole());
        System.out.println("✅ Responsable saved: " + saved.getId());
    }

    @Test
    void testFindUtilisateurByEmail() {
        Utilisateur u = Utilisateur.builder()
                .nom("Idrissi").prenom("Youssef")
                .email("youssef.idrissi@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.PRESIDENT_CLUB).actif(true)
                .build();
        utilisateurRepository.save(u);

        Optional<Utilisateur> found = utilisateurRepository.findByEmail("youssef.idrissi@unievt.ma");
        assertTrue(found.isPresent());
        assertEquals("Idrissi", found.get().getNom());
        System.out.println("✅ Found by email: " + found.get().getEmail());
    }

    @Test
    void testExistsByEmail() {
        Utilisateur u = Utilisateur.builder()
                .nom("Moussaoui").prenom("Sara")
                .email("sara.moussaoui@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.ADMIN).actif(true)
                .build();
        utilisateurRepository.save(u);

        assertTrue(utilisateurRepository.existsByEmail("sara.moussaoui@unievt.ma"));
        assertFalse(utilisateurRepository.existsByEmail("nonexistent@unievt.ma"));
        System.out.println("✅ existsByEmail works correctly");
    }

    @Test
    void testUtilisateurDateCreationAutoSet() {
        Utilisateur u = Utilisateur.builder()
                .nom("Bennis").prenom("Omar")
                .email("omar.bennis@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.ETUDIANT).actif(true)
                .build();

        Utilisateur saved = utilisateurRepository.save(u);
        utilisateurRepository.flush();
        assertNotNull(saved.getDateCreation());
        System.out.println("✅ dateCreation auto-set: " + saved.getDateCreation());
    }

    // ─────────────────────────────────────────────
    //  ETUDIANT
    // ─────────────────────────────────────────────

    @Test
    void testSaveEtudiant() {
        Etudiant e = Etudiant.builder()
                .nom("Alaoui").prenom("Fatima")
                .email("fatima.alaoui@unievt.ma")
                .motDePasse("etu123")
                .role(RoleEnum.ETUDIANT).actif(true)
                .filiere("Génie Informatique")
                .anneeEtude(2).cin("AB123456")
                .build();

        Etudiant saved = etudiantRepository.save(e);
        assertNotNull(saved.getId());
        assertEquals("Génie Informatique", saved.getFiliere());
        System.out.println("✅ Etudiant saved: " + saved.getId());
    }

    @Test
    void testSaveMultipleEtudiants() {
        Etudiant e1 = Etudiant.builder()
                .nom("Ziani").prenom("Mehdi")
                .email("mehdi.ziani@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.ETUDIANT).actif(true)
                .filiere("Génie Civil").anneeEtude(1).cin("CD111111")
                .build();

        Etudiant e2 = Etudiant.builder()
                .nom("Tahiri").prenom("Amine")
                .email("amine.tahiri@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.ETUDIANT).actif(true)
                .filiere("Génie Électrique").anneeEtude(3).cin("EF222222")
                .build();

        Etudiant e3 = Etudiant.builder()
                .nom("Benhaddou").prenom("Salma")
                .email("salma.benhaddou@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.ETUDIANT).actif(true)
                .filiere("Génie Informatique").anneeEtude(4).cin("GH333333")
                .build();

        etudiantRepository.saveAll(List.of(e1, e2, e3));
        assertEquals(3, etudiantRepository.findAll().size());
        System.out.println("✅ 3 etudiants saved");
    }

    @Test
    void testFindEtudiantByCin() {
        Etudiant e = Etudiant.builder()
                .nom("Kettani").prenom("Rania")
                .email("rania.kettani@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.ETUDIANT).actif(true)
                .filiere("Génie Informatique").anneeEtude(2).cin("ZZ999999")
                .build();
        etudiantRepository.save(e);

        Optional<Etudiant> found = etudiantRepository.findByCin("ZZ999999");
        assertTrue(found.isPresent());
        assertEquals("Kettani", found.get().getNom());
        System.out.println("✅ Found etudiant by CIN: " + found.get().getCin());
    }

    @Test
    void testEtudiantIsAlsoUtilisateur() {
        Etudiant e = Etudiant.builder()
                .nom("Fassi").prenom("Karim")
                .email("karim.fassi@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.ETUDIANT).actif(true)
                .filiere("Génie Informatique").anneeEtude(1).cin("KF000001")
                .build();
        etudiantRepository.save(e);

        // should also be findable as a Utilisateur
        Optional<Utilisateur> found = utilisateurRepository.findByEmail("karim.fassi@unievt.ma");
        assertTrue(found.isPresent());
        assertInstanceOf(Etudiant.class, found.get());
        System.out.println("✅ Etudiant is also a Utilisateur (inheritance works)");
    }

    // ─────────────────────────────────────────────
    //  CLUB
    // ─────────────────────────────────────────────

    @Test
    void testSaveClub() {
        Utilisateur president = Utilisateur.builder()
                .nom("Hassoub").prenom("Strike")
                .email("strike@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.PRESIDENT_CLUB).actif(true)
                .build();
        utilisateurRepository.save(president);

        Club club = Club.builder()
                .nom("Club IA")
                .description("Club d'intelligence artificielle")
                .categorie("Technologie").actif(true)
                .president(president)
                .build();

        Club saved = clubRepository.save(club);
        assertNotNull(saved.getId());
        assertEquals("Club IA", saved.getNom());
        System.out.println("✅ Club saved: " + saved.getId());
    }

    @Test
    void testSaveMultipleClubs() {
        Utilisateur p1 = Utilisateur.builder()
                .nom("Amrani").prenom("Karim")
                .email("karim.amrani@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.PRESIDENT_CLUB).actif(true)
                .build();

        Utilisateur p2 = Utilisateur.builder()
                .nom("Berrada").prenom("Leila")
                .email("leila.berrada@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.PRESIDENT_CLUB).actif(true)
                .build();

        utilisateurRepository.saveAll(List.of(p1, p2));

        Club c1 = Club.builder().nom("Club Robotique")
                .description("Robotique et automatisme")
                .categorie("Technologie").actif(true).president(p1).build();

        Club c2 = Club.builder().nom("Club Théâtre")
                .description("Art dramatique")
                .categorie("Culture").actif(true).president(p2).build();

        Club c3 = Club.builder().nom("Club Sport")
                .description("Activités sportives")
                .categorie("Sport").actif(false).president(p1).build();

        clubRepository.saveAll(List.of(c1, c2, c3));
        assertEquals(3, clubRepository.findAll().size());
        System.out.println("✅ 3 clubs saved");
    }

    @Test
    void testFindActiveClubs() {
        Utilisateur p = Utilisateur.builder()
                .nom("Tahir").prenom("Nour")
                .email("nour.tahir@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.PRESIDENT_CLUB).actif(true)
                .build();
        utilisateurRepository.save(p);

        clubRepository.save(Club.builder().nom("Club Actif 1").categorie("Tech").actif(true).president(p).build());
        clubRepository.save(Club.builder().nom("Club Actif 2").categorie("Sport").actif(true).president(p).build());
        clubRepository.save(Club.builder().nom("Club Inactif").categorie("Culture").actif(false).president(p).build());

        List<Club> activeClubs = clubRepository.findByActifTrue();
        assertEquals(2, activeClubs.size());
        System.out.println("✅ Found " + activeClubs.size() + " active clubs");
    }

    @Test
    void testFindClubsByCategorie() {
        Utilisateur p = Utilisateur.builder()
                .nom("Lahlou").prenom("Rim")
                .email("rim.lahlou@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.PRESIDENT_CLUB).actif(true)
                .build();
        utilisateurRepository.save(p);

        clubRepository.save(Club.builder().nom("Club IA").categorie("Technologie").actif(true).president(p).build());
        clubRepository.save(Club.builder().nom("Club Web").categorie("Technologie").actif(true).president(p).build());
        clubRepository.save(Club.builder().nom("Club Foot").categorie("Sport").actif(true).president(p).build());

        List<Club> techClubs = clubRepository.findByCategorie("Technologie");
        assertEquals(2, techClubs.size());
        System.out.println("✅ Found " + techClubs.size() + " tech clubs");
    }

    @Test
    void testClubPresidentRelationship() {
        Utilisateur president = Utilisateur.builder()
                .nom("Chraibi").prenom("Adam")
                .email("adam.chraibi@unievt.ma")
                .motDePasse("pass123")
                .role(RoleEnum.PRESIDENT_CLUB).actif(true)
                .build();
        utilisateurRepository.save(president);

        Club club = Club.builder()
                .nom("Club Dev").categorie("Tech").actif(true)
                .president(president).build();
        Club saved = clubRepository.save(club);

        assertNotNull(saved.getPresident());
        assertEquals("Chraibi", saved.getPresident().getNom());
        System.out.println("✅ Club president relationship works: " + saved.getPresident().getEmail());
    }

    // ─────────────────────────────────────────────
    //  INTERVENANT
    // ─────────────────────────────────────────────

    @Test
    void testSaveIntervenant() {
        Intervenant i = Intervenant.builder()
                .nom("Bensouda")
                .institution("MIT").biographie("Expert en IA et ML")
                .build();

        Intervenant saved = intervenantRepository.save(i);
        assertNotNull(saved.getId());
        assertEquals("MIT", saved.getInstitution());
        System.out.println("✅ Intervenant saved: " + saved.getId());
    }

    @Test
    void testSaveMultipleIntervenants() {
        Intervenant i1 = Intervenant.builder().nom("Dupont")
                .institution("École Polytechnique").biographie("Spécialiste en cybersécurité").build();
        Intervenant i2 = Intervenant.builder().nom("Garcia")
                .institution("Stanford").biographie("Chercheur en NLP").build();
        Intervenant i3 = Intervenant.builder().nom("Smith")
                .institution("Oxford").biographie("Expert en blockchain").build();

        intervenantRepository.saveAll(List.of(i1, i2, i3));
        assertEquals(3, intervenantRepository.findAll().size());
        System.out.println("✅ 3 intervenants saved");
    }

    @Test
    void testDeleteIntervenant() {
        Intervenant i = Intervenant.builder()
                .nom("Temporary").institution("FST").biographie("To be deleted").build();
        Intervenant saved = intervenantRepository.save(i);
        Long id = saved.getId();

        intervenantRepository.deleteById(id);
        assertFalse(intervenantRepository.existsById(id));
        System.out.println("✅ Intervenant deleted successfully");
    }

    @Test
    void testUpdateIntervenant() {
        Intervenant i = Intervenant.builder()
                .nom("OldName").institution("OldInstitution").biographie("Old bio").build();
        Intervenant saved = intervenantRepository.save(i);

        saved.setNom("NewName");
        saved.setInstitution("NewInstitution");
        Intervenant updated = intervenantRepository.save(saved);

        assertEquals("NewName", updated.getNom());
        assertEquals("NewInstitution", updated.getInstitution());
        System.out.println("✅ Intervenant updated successfully");
    }
}