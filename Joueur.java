public class Joueur {

    private String nom;
    private FeuilleDeMarque fdm;
    private int tour;
    private int Score;

    public Joueur(String nom) {
        this.nom = nom;
        this.fdm = null;
        this.tour = 0;
        this.Score = 0;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getScore() throws Exception{
        Case[] tabCases = this.getFDMCase();
        int size = tabCases.length;
        int score = 0;
        for (int i = 0; i < size; i++) {
            Case c = tabCases[i];
            if(c.getEtat() == EtatCase.REMPLIE){
                score += Integer.parseInt(c.getValeur());
            }else if(c.getEtat() == EtatCase.BONUS){
                // DOUBLE VERIFICATION EN CAS D'ERREUR DE MANIPULATION
                if(!(c instanceof CaseBonus)) throw new Exception("Une case en état Bonus n'est pas une sous-classe CaseBonus");
                CaseBonus bonus = (CaseBonus) c;
                if(bonus.getCondition() < score) score += bonus.getBonus();
            }
        }this.setScore(score);
        return this.Score;
    }

    public void actualize() throws Exception{
        Case[] tabCases = this.getFDMCase();
        int size = tabCases.length;
        int score = 0;
        for (int i = 0; i < size; i++) {
            Case c = tabCases[i];
            if (c.getEtat() == EtatCase.SPECIALE){
                this.getFDM().setValueCase(i, score);
            }else if(c.getEtat() == EtatCase.REMPLIE){
                score += Integer.parseInt(c.getValeur());
            }else if(c.getEtat() == EtatCase.BONUS){
                // DOUBLE VERIFICATION EN CAS D'ERREUR DE MANIPULATION
                if(!(c instanceof CaseBonus)) throw new Exception("Une case en état Bonus n'est pas une sous-classe CaseBonus");
                CaseBonus bonus = (CaseBonus) c;
                if(bonus.getCondition() < score) bonus.setValeur(bonus.getBonus()); score += bonus.getBonus();
            }
        }
    }

    private void setScore(int score) {
        Score = score;
    }

    public FeuilleDeMarque getFDM() {
        return fdm;
    }

    public void setFdm(FeuilleDeMarque fdm) {
        this.fdm = fdm;
    }

    public int getTour() {
        return tour;
    }

    public void addTour() {
        this.tour += 1;
    }

    @Override
    public String toString() {
        try{
        return this.getNom() + "\nScore : " + this.getScore() + "\nFeuille de marque :\n" + this.getFDM().toString();
        }catch(Exception e){
            System.out.println(e);
            e.printStackTrace();
            return "";
        }
    }

    public Case[] getFDMCase() {
        return this.fdm.getAllCases();
    }

}
