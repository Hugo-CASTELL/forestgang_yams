public class Case {
    private String nom;
    private EtatCase etat;
    private String valeur;

    public Case(String nom, EtatCase etat) {
        this.nom = nom;
        this.etat = etat;
        this.valeur = "";
    }

    public Case(String nom, EtatCase etat, String valeur) {
        this.nom = nom;
        this.etat = etat;
        this.valeur = valeur;
    }

    public String getNom() {
        return this.nom;
    }

    public EtatCase getEtat() {
        return this.etat;
    }

    public String getValeur() {
        return this.valeur;
    }

    public void setEtat(EtatCase etat) {
        if(this.etat != EtatCase.SPECIALE) this.etat = etat;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setValeur(int valeur) {
        this.valeur = String.valueOf(valeur);
    }

}
