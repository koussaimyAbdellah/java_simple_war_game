package completed_working_code;
import java.util.Scanner;

/**
 * CORRECTED VERSION - War Game
 * Key improvements:
 * - Removed code duplication (DRY principle)
 * - Fixed logic bugs (health going negative, game flow)
 * - Added constants instead of magic numbers
 * - Fixed menu flow
 * - Better naming and structure
 * - Added comments for learning
 */

// Base class for all characters
abstract class Person implements Attaquable {
    protected String nom;
    protected int pointsDeVie;
    
    public Person(String nom, int pointsDeVie) {
        this.nom = nom;
        this.pointsDeVie = pointsDeVie;
    }
    
    // Each character type must implement their own attack
    public abstract int attaquer();
    
    public void jeSuis() {
        System.out.println("Je suis " + this.nom + ".");
    }
    
    public int getPointsDeVie() {
        return this.pointsDeVie;
    }
    
    public void setPointsDeVie(int points) {
        this.pointsDeVie = Math.max(0, points); // Never negative
    }
    
    public boolean estVivant() {
        return this.pointsDeVie > 0;
    }
    
    // IMPROVEMENT: Moved from child classes to avoid duplication
    @Override
    public void recevoirDegats(int degats) {
        int nouveauxPoints = this.pointsDeVie - degats;
        if (nouveauxPoints > 0) {
            this.pointsDeVie = nouveauxPoints;
            System.out.println(this.nom + " a maintenant " + nouveauxPoints + " points de vie.");
        } else {
            this.pointsDeVie = 0;
            System.out.println(this.nom + " est mort!");
        }
    }
}

interface Attaquable {
    void recevoirDegats(int points);
}

// Character types
class Guerrier extends Person {
    private static final int DEGATS = 10;
    private static final int VIE_INITIALE = 100;
    
    public Guerrier() {
        super("Guerrier", VIE_INITIALE);
    }
    
    @Override
    public int attaquer() {
        System.out.println(this.nom + " attaque avec son épée! ⚔️  Dégâts: " + DEGATS);
        return DEGATS;
    }
}

class Magicien extends Person {
    private static final int DEGATS = 15;
    private static final int VIE_INITIALE = 100;
    
    public Magicien() {
        super("Magicien", VIE_INITIALE);
    }
    
    @Override
    public int attaquer() {
        System.out.println(this.nom + " lance un sort magique! ✨  Dégâts: " + DEGATS);
        return DEGATS;
    }
}

class Archer extends Person {
    private static final int DEGATS = 5;
    private static final int VIE_INITIALE = 100;
    
    public Archer() {
        super("Archer", VIE_INITIALE);
    }
    
    @Override
    public int attaquer() {
        System.out.println(this.nom + " tire une flèche! 🏹  Dégâts: " + DEGATS);
        return DEGATS;
    }
}

class Soldats {
    // IMPROVEMENT: Constants instead of magic numbers
    public static final int GUERRIER = 1;
    public static final int MAGICIEN = 2;
    public static final int ARCHER = 3;
    
    private Guerrier guerrier;
    private Magicien magicien;
    private Archer archer;
    
    public Soldats() {
        this.guerrier = new Guerrier();
        this.magicien = new Magicien();
        this.archer = new Archer();
    }
    
    // IMPROVEMENT: Better method name and error handling
    public int effectuerAttaque(int choixSoldat) {
        switch (choixSoldat) {
            case GUERRIER:
                return guerrier.attaquer();
            case MAGICIEN:
                return magicien.attaquer();
            case ARCHER:
                return archer.attaquer();
            default:
                System.out.println("Choix invalide! Aucune attaque effectuée.");
                return 0;
        }
    }
    
    // IMPROVEMENT: Now uses the recevoirDegats method instead of manual calculation
    public void recevoirDegatsEnnemi(int degats, int choixCible) {
        switch (choixCible) {
            case GUERRIER:
                guerrier.recevoirDegats(degats);
                break;
            case MAGICIEN:
                magicien.recevoirDegats(degats);
                break;
            case ARCHER:
                archer.recevoirDegats(degats);
                break;
            default:
                System.out.println("Cible invalide!");
        }
    }
    
    // IMPROVEMENT: Better method name (estVivant instead of estLeGroupVivre)
    public boolean estVivant() {
        return guerrier.estVivant() || magicien.estVivant() || archer.estVivant();
    }
    
    // IMPROVEMENT: Added method to display group status
    public void afficherStatut() {
        System.out.println("  Guerrier: " + guerrier.getPointsDeVie() + " PV");
        System.out.println("  Magicien: " + magicien.getPointsDeVie() + " PV");
        System.out.println("  Archer: " + archer.getPointsDeVie() + " PV");
    }
}

class ZoneDeCombat {
    private Soldats groupe1;
    private Soldats groupe2;
    private boolean tourGroupe1;
    
    public ZoneDeCombat() {
        this.groupe1 = new Soldats();
        this.groupe2 = new Soldats();
        this.tourGroupe1 = true;
    }
    
    public boolean estTourGroupe1() {
        return this.tourGroupe1;
    }
    
    public void effectuerAttaque(int choixAttaquant, int choixDefenseur) {
        if (tourGroupe1) {
            System.out.println("\n>>> Groupe 1 attaque Groupe 2!");
            int degats = groupe1.effectuerAttaque(choixAttaquant);
            groupe2.recevoirDegatsEnnemi(degats, choixDefenseur);
            this.tourGroupe1 = false;
        } else {
            System.out.println("\n>>> Groupe 2 attaque Groupe 1!");
            int degats = groupe2.effectuerAttaque(choixAttaquant);
            groupe1.recevoirDegatsEnnemi(degats, choixDefenseur);
            this.tourGroupe1 = true;
        }
    }
    
    // IMPROVEMENT: Fixed logic - returns true when game should CONTINUE
    public boolean partieEnCours() {
        return groupe1.estVivant() && groupe2.estVivant();
    }
    
    // IMPROVEMENT: Better method name
    public String obtenirVainqueur() {
        if (groupe1.estVivant() && !groupe2.estVivant()) {
            return "Groupe 1";
        } else if (!groupe1.estVivant() && groupe2.estVivant()) {
            return "Groupe 2";
        } else if (!groupe1.estVivant() && !groupe2.estVivant()) {
            return "Match nul (tous morts)";
        } else {
            return "Erreur: les deux groupes sont vivants";
        }
    }
    
    // IMPROVEMENT: Added method to show game state
    public void afficherEtatDuJeu() {
        System.out.println("\n=== État du Combat ===");
        System.out.println("Groupe 1:");
        groupe1.afficherStatut();
        System.out.println("\nGroupe 2:");
        groupe2.afficherStatut();
        System.out.println("=====================\n");
    }
}

class JeuDeGuerre {
    private ZoneDeCombat zone;
    private int nombreDeRounds;
    private int choix;
    
    public JeuDeGuerre() {
        this.zone = new ZoneDeCombat();
        this.nombreDeRounds = 0;
    }
    
    public void introduction() {
        System.out.println("╔═══════════════════════════════════════╗");
        System.out.println("║   BIENVENUE AU JEU DE GUERRE!         ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\nVous avez deux groupes: Groupe 1 et Groupe 2");
        System.out.println("Chaque groupe a 3 soldats:");
        System.out.println("  1. Guerrier (10 dégâts) ⚔️");
        System.out.println("  2. Magicien (15 dégâts) ✨");
        System.out.println("  3. Archer (5 dégâts) 🏹");
        System.out.println("\nObjectif: Éliminer tous les soldats ennemis!");
        System.out.println("Le premier groupe à éliminer l'ennemi gagne.\n");
    }
    
    public void afficherMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          MENU PRINCIPAL                ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  1. Commencer la partie                ║");
        System.out.println("║  2. Afficher les règles                ║");
        System.out.println("║  3. Quitter                            ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
    
    public void afficherMenuJeu() {
        System.out.println("\n--- Options de Tour ---");
        System.out.println("1. Attaquer");
        System.out.println("2. Abandonner le round");
    }
    
    // IMPROVEMENT: Separated input validation into helper method
    private int lireChoix(Scanner sc, String message) {
        System.out.print(message);
        while (!sc.hasNextInt()) {
            System.out.print("Veuillez entrer un nombre valide: ");
            sc.next();
        }
        int choix = sc.nextInt();
        sc.nextLine(); // Consume newline
        return choix;
    }
    
    // IMPROVEMENT: Fixed game loop logic and menu flow
    public void traiterChoix(Scanner sc) {
        this.choix = lireChoix(sc, "Votre choix: ");
        
        switch (this.choix) {
            case 1:
                jouerPartie(sc);
                break;
            case 2:
                introduction();
                break;
            case 3:
                System.out.println("\nMerci d'avoir joué! À bientôt! 👋\n");
                break;
            default:
                System.out.println("Choix invalide. Veuillez choisir 1, 2 ou 3.");
        }
    }
    
    // IMPROVEMENT: Extracted game loop into separate method
    private void jouerPartie(Scanner sc) {
        System.out.println("\n🎮 La partie commence!\n");
        zone = new ZoneDeCombat(); // Reset game state
        nombreDeRounds = 0;
        
        // IMPROVEMENT: Fixed loop condition - continues while game is active
        while (zone.partieEnCours()) {
            nombreDeRounds++;
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║        ROUND " + nombreDeRounds + "                           ║");
            System.out.println("╚════════════════════════════════════════╝");
            
            String tourActuel = zone.estTourGroupe1() ? "Groupe 1" : "Groupe 2";
            System.out.println("Tour de: " + tourActuel);
            
            zone.afficherEtatDuJeu();
            
            afficherMenuJeu();
            int choixJeu = lireChoix(sc, "Votre action: ");
            
            if (choixJeu == 1) {
                // Choose attacker
                System.out.println("\nQuel soldat va attaquer?");
                System.out.println("1. Guerrier  2. Magicien  3. Archer");
                int choixAttaquant = lireChoix(sc, "Votre choix: ");
                
                // Validate attacker choice
                if (choixAttaquant < 1 || choixAttaquant > 3) {
                    System.out.println("Choix invalide! Tour perdu.");
                    continue;
                }
                
                // Choose defender
                System.out.println("\nQuel soldat ennemi voulez-vous cibler?");
                System.out.println("1. Guerrier  2. Magicien  3. Archer");
                int choixDefenseur = lireChoix(sc, "Votre choix: ");
                
                // Validate defender choice
                if (choixDefenseur < 1 || choixDefenseur > 3) {
                    System.out.println("Choix invalide! Tour perdu.");
                    continue;
                }
                
                zone.effectuerAttaque(choixAttaquant, choixDefenseur);
                
            } else if (choixJeu == 2) {
                System.out.println("\n❌ Vous avez abandonné au round " + nombreDeRounds);
                return;
            } else {
                System.out.println("Choix invalide. Tour perdu.");
            }
        }
        
        // IMPROVEMENT: Game end message
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          FIN DE LA PARTIE              ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("🏆 Vainqueur: " + zone.obtenirVainqueur());
        System.out.println("📊 Nombre de rounds: " + nombreDeRounds);
        zone.afficherEtatDuJeu();
    }
    
    public int getChoix() {
        return this.choix;
    }
}

public class WarGame {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        JeuDeGuerre jeu = new JeuDeGuerre();
        
        // IMPROVEMENT: Show introduction once at start
        jeu.introduction();
        
        // IMPROVEMENT: Fixed menu flow - no duplicate displays
        while (true) {
            jeu.afficherMenuPrincipal();
            jeu.traiterChoix(sc);
            
            if (jeu.getChoix() == 3) {
                break;
            }
        }
        
        sc.close();
    }
}
