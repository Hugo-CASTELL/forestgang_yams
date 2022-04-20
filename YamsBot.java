/**
 * Moteur de jeu de Yams : 
 * 
 * Débuter une partie :
 *      Le constructeur initialise la partie et crée les Joueurs en fonction du nombre de joueurs souhaité (>=2)
 *      Le nom des joueurs est null par défaut, il est possible de les modifier en appelant Joueur.setNom()
 * 
 * Début d'un tour :
 *      EtatBot.WAIT :
 *          Attends que l'état EtatBot.JEU soit lancé
 *          initialisé en tant qu'état de départ les méthodes citées plus tard
 *      EtatBot.JEU :
 *          tour(Joueur joueur) initialise un tour de jeu parmi la liste des joueurs pour un joueur dont l'index est renseigné
 *          Rajoute au Joueur un tour de jeu et mets le bot en Etat EtatBot.JEU
 *          Le joueur peut fixerDe() et libererDe() pour, au choix, éviter de le relancer ou non
 *          Pour l'implémentation finale, vous pouvez utiliser invertEtatDe() pour changer automatiquement l'état
 *      EtatBot.FIXATION :
 *          À l'appel de la méthode garder(), le jeu se met en état EtatBot.FIXATION
 *          Le joueur peut alors choisir de fixer une case grâce à fixer(Case case) ou de la barrer(Case case)
 *          Les méthodes de calculs spéciaux se nomment par leur nom : brelan(), full()... et renvoient des types différents
 *          (Elles se trouvent en bas de ce fichier ou en javadoc)
 *          Le jeu repasse de nouveau en état EtatBot.WAIT
 * 
 * Calcul du score d'un joueur :
 *      Joueur.getScore() calcule automatiquement le score du joueur et renvoie son score total
 *      A différencier de Joueur.actualize() qui actualise les cases spéciales et cases bonus
 * 
 * Récupération des faces des dés :
 *      La méthode faceDes() renvoie un tableau contenant dans l'ordre la valeur de la face des dés.
 * 
 * Récupération d'un score de joueur :
 *      Format String :
 *          Joueur.toString() permet d'obtenir une représentation String du joueur et de sa feuille de marque
 *          Joueur.getFDM().toString() permet d'obtenir uniquement la feuille de marque du joueur en String
 *      Format Case :
 *          Joueur.getFDMcase() retourne un tableau de Case de la feuille de marque correspondante
 * 
 * @author : Hugo CASTELL
 */
public class YamsBot {
    private Joueur[] joueurs;
    private De[] des;
    private final int nbDes = 5;
    private EtatBot etat;

    /**
     * Constructeur paramétré qui initialise le nombre de joueurs à nbJoueurs
     * 
     * @param nbJoueurs
     * @throws BotException si le nombre de joueurs est inférieur à 2
     */
    public YamsBot(int nbJoueurs) throws BotException {
        if (nbJoueurs < 2)
            throw new BotException("Le nombre de joueurs est insuffisant");
        this.joueurs = new Joueur[nbJoueurs];
        for (int i = 0; i < nbJoueurs; i++)
            this.joueurs[i] = newJoueur();
        for (int i = 0; i < nbDes; i++)
            this.des[i] = new De(EtatDe.LIBRE);
        this.setEtat(EtatBot.WAIT);
    }

    /**
     * Renvoie un tableau à deux dimensions. <BR>
     * Première dimension : indice des dés dans le tableau correspondant du bot
     * Seconde dimension : face du dé au même indice
     * 
     * @return int[][]
     * 
     * @throws BotException si aucun dé ne peut être lancé
     */
    public int[][] lancerLesDes(Joueur joueur) throws BotException {
        int nbDesNonFixes = 0;
        for (int i = 0; i < nbDes; i++)
            if (this.des[i].getEtat() != EtatDe.FIXE)
                nbDesNonFixes += 1;
        if (nbDesNonFixes == 0)
            throw new BotException("Aucun dé ne peut être lancé.");
        int[][] des = new int[nbDesNonFixes][2];
        for (int i = 0; i < nbDes; i++)
            if (this.des[i].getEtat() != EtatDe.FIXE) {
                des[nbDesNonFixes][0] = i;
                des[nbDesNonFixes][1] = this.des[i].actualize();
            }
        return des;
    }

    /**
     * Fixe un dé dont l'index dans this.des est rentré en paramètre. <BR>
     * 
     * @param index
     * @throws BotException si l'index n'est pas valide
     */
    public void fixerDe(int index) throws BotException {
        if (index >= nbDes || index < 0)
            throw new BotException("Index de dé invalide");
        this.des[index].setEtat(EtatDe.FIXE);
    }

    /**
     * Libère un dé dont l'index dans this.des est rentré en paramètre. <BR>
     * 
     * @param index
     * @throws BotException si l'index n'est pas valide
     */
    public void libererDe(int index) throws BotException {
        if (index >= nbDes || index < 0)
            throw new BotException("Index de dé invalide");
        this.des[index].setEtat(EtatDe.LIBRE);
    }

    /**
     * Inverse l'état d'un dé dont l'index dans this.des est rentré en paramètre.
     * <BR>
     * 
     * @param index
     * @throws BotException si l'index n'est pas valide
     */
    public void invertEtatDe(int index) throws BotException {
        if (index >= nbDes || index < 0)
            throw new BotException("Index de dé invalide");
        if (this.des[index].getEtat() == EtatDe.LIBRE) {
            this.des[index].setEtat(EtatDe.FIXE);
        } else {
            this.des[index].setEtat(EtatDe.LIBRE);
        }
    }

    public void garder() {
        this.setEtat(EtatBot.FIXATION);
    }

    /**
     * @return int[]
     */
    public int[] faceDes() {
        return new int[] { this.des[0].getFace(), this.des[1].getFace(), this.des[2].getFace(), this.des[3].getFace(),
                this.des[4].getFace() };
    }

    /**
     * @param joueur
     * @throws BotException
     */
    public void tour(Joueur joueur) throws BotException {
        if (this.getEtat() != EtatBot.WAIT)
            throw new BotException("Le bot n'était pas en état WAIT quand le tour a été initialisé");
        this.libererDes();
        this.setEtat(EtatBot.JEU);
        joueur.addTour();
    }

    private void libererDes() {
        for (int i = 0; i < nbDes; i++)
            this.des[i].setEtat(EtatDe.LIBRE);
    }

    public void fixer(Case c){
        c.setEtat(EtatCase.REMPLIE);
    }

    public void barrer(Case c){
        c.setEtat(EtatCase.BARREE);
    }

    /**
     * @return Joueur
     */
    private Joueur newJoueur() {
        Joueur newPlayer = new Joueur(null);
        newPlayer.setFdm(this.feuilleInitiale());
        return newPlayer;
    }

    /**
     * @return FeuilleDeMarque
     */
    private FeuilleDeMarque feuilleInitiale() {
        FeuilleDeMarque fdm = new FeuilleDeMarque();
        Case[] cases = new Case[] {
                new Case("Total de 1", EtatCase.VIDE),
                new Case("Total de 2", EtatCase.VIDE),
                new Case("Total de 3", EtatCase.VIDE),
                new Case("Total de 4", EtatCase.VIDE),
                new Case("Total de 5", EtatCase.VIDE),
                new Case("Total de 6", EtatCase.VIDE),
                new Case("Total", EtatCase.SPECIALE),
                new CaseBonus("Bonus de 35 points si Total >= 63", EtatCase.BONUS, 35, 63),
                new Case("TOTAL DE LA PARTIE INTERMEDIAIRE", EtatCase.SPECIALE),
                new Case("Brelan", EtatCase.VIDE),
                new Case("Carré", EtatCase.VIDE),
                new Case("Full", EtatCase.VIDE),
                new Case("Petite Suite", EtatCase.VIDE),
                new Case("Grande Suite", EtatCase.VIDE),
                new Case("Yams", EtatCase.VIDE),
                new Case("Chance", EtatCase.VIDE),
                new Case("TOTAL DE LA SECONDE PARTIE", EtatCase.VIDE),
                new Case("TOTAL FINAL", EtatCase.VIDE)
        };
        try {
            fdm.setCases(cases);
        } catch (Exception e) {
            System.out.println("Problème de chargement des feuilles de marque");
            e.printStackTrace();
        }
        return fdm;
    }

    /**
     * Méthode de score : renvoie -1 comme code erreur
     * 
     * brelan() renvoie la somme des faces des dés s'il détecte un brelan
     * Un brelan correspond à 3 faces identiques et des valeurs uniques différentes
     * parmi les autres faces
     * Exemple : 6-6-6 2-5 : 25
     * 3-3-3 1-4 : 14
     * 
     * @return int
     */
    public int brelan() {
        int[] tabFaces = this.faceDes();
        int[] faceDunDe = new int[nbDes];
        int score = 0;
        for (int i = 0; i < nbDes; i++)
            faceDunDe[i] = 0;
        for (int i = 0; i < nbDes; i++) {
            faceDunDe[tabFaces[i] - 1] += 1;
            score += tabFaces[i];
        }
        for (int i = 0; i < nbDes; i++) {
            if (faceDunDe[i] >= 3) { // Brelan
                return score;
            }
        }
        return -1;
    }

    /**
     * Méthode de score : renvoie -1 comme code erreur
     * 
     * carre() renvoie un score correspondant à 4*val.face s'il détecte un carré
     * d'une face
     * Un carré correspond à 4 faces identiques parmi toutes les faces des dés
     * Exemple : 4-4-4-4 : 16
     * 1-1-1-1 : 4
     * 
     * @return int
     */
    public int carre() {
        int[] tabFaces = this.faceDes();
        int[] faceDunDe = new int[nbDes];
        for (int i = 0; i < nbDes; i++)
            faceDunDe[i] = 0;
        for (int i = 0; i < nbDes; i++) {
            faceDunDe[tabFaces[i] - 1] += 1;
        }
        for (int i = 0; i < nbDes; i++) {
            if (faceDunDe[i] >= 4)
                return (i + 1) * 4;
        }
        return -1;
    }

    /**
     * Méthode de score : renvoie -1 comme code erreur
     * 
     * full() renvoie 25 s'il détecte un full
     * Un full correspond trois faces identiques et deux faces différentes
     * elles-mêmes identiques
     * Exemple : 6-6-6 2-2
     * 5-5-5 1-1
     * 
     * @return int
     */
    public int full() {
        int[] tabFaces = this.faceDes();
        int[] faceDunDe = new int[nbDes];
        for (int i = 0; i < nbDes; i++)
            faceDunDe[i] = 0;
        for (int i = 0; i < nbDes; i++) {
            faceDunDe[tabFaces[i] - 1] += 1;
        }
        for (int i = 0; i < nbDes; i++) {
            if (faceDunDe[i] >= 3) { // Brelan
                for (int j = 0; i < nbDes; j++) {
                    if (faceDunDe[j] == 2)
                        return 25;
                }
            }
        }
        return -1;
    }

    /**
     * Méthode de score : renvoie -1 comme code erreur
     * 
     * petiteSuite() renvoie 30 s'il détecte une grande suite
     * Une petite suite correspond à ce que quatre valeurs
     * toutes les valeurs des faces soient croissantes 1 à 1
     * Exemple : 1-2-3-4
     * 3-4-5-6
     * 2-3-4-5
     * 
     * @return int
     */
    public int petiteSuite() {
        int[] tabFaces = this.faceDes();
        int[] faceDunDe = new int[nbDes];
        for (int i = 0; i < nbDes; i++)
            faceDunDe[i] = 0;
        for (int i = 0; i < nbDes; i++) {
            if (faceDunDe[tabFaces[i] - 1] == 0)
                faceDunDe[tabFaces[i] - 1] += 1;
        }
        // BOUCLE FOR A NBDES
        // UTILISER COMPTEUR
        // SI 5 SE SUIVENT : GRANDE SUITE
        int compteur = 0;
        for (int i = 1; i < nbDes; i++) {
            if (faceDunDe[i] - faceDunDe[i - 1] == 0) {
                compteur += 1;
            } else {
                compteur = 0;
            }
            if (compteur == 4)
                return 30;
        }
        return -1;
    }

    /**
     * Méthode de score : renvoie -1 comme code erreur
     * 
     * grandeSuite() renvoie 40 s'il détecte une grande suite
     * Une grande suite correspond à ce que toutes les valeurs des faces soient
     * croissantes 1 à 1
     * Exemple : 1-2-3-4-5
     * 2-3-4-5-6
     * 
     * @return int
     */
    public int grandeSuite() {
        int[] tabFaces = this.faceDes();
        int[] faceDunDe = new int[nbDes];
        for (int i = 0; i < nbDes; i++)
            faceDunDe[i] = 0;
        for (int i = 0; i < nbDes; i++) {
            if (faceDunDe[tabFaces[i] - 1] == 0)
                faceDunDe[tabFaces[i] - 1] += 1;
        }
        // BOUCLE FOR A NBDES
        // UTILISER COMPTEUR
        // SI 5 SE SUIVENT : GRANDE SUITE
        int compteur = 0;
        for (int i = 1; i < nbDes; i++) {
            if (faceDunDe[i] - faceDunDe[i - 1] == 0) {
                compteur += 1;
            } else {
                compteur = 0;
            }
            if (compteur == nbDes)
                return 40;
        }
        return -1;
    }

    /**
     * Méthode de score : renvoie -1 comme code erreur
     * 
     * yams() renvoie 50 s'il détecte un Yams
     * Un Yams correspond à tous les dés de même face
     * 
     * @return int
     */
    public int yams() {
        int[] tabFaces = this.faceDes();
        int[] faceDunDe = new int[nbDes];
        for (int i = 0; i < nbDes; i++)
            faceDunDe[i] = 0;
        for (int i = 0; i < nbDes; i++) {
            faceDunDe[tabFaces[i] - 1] += 1;
        }
        for (int i = 0; i < nbDes; i++) {
            if (faceDunDe[i] >= 5)
                return 50;
        }
        return -1;
    }

    /**
     * Renvoie le score de la chance. <BR>
     * 
     * @return int
     */
    public int chance() {
        int[] tabFaces = this.faceDes();
        int score = 0;
        for (int i = 0; i < nbDes; i++)
            score += tabFaces[i];
        return score;
    }

    public EtatBot getEtat() {
        return etat;
    }

    private void setEtat(EtatBot etat) {
        this.etat = etat;
    }
}