import java.util.ArrayList;

public class FeuilleDeMarque {
    private ArrayList<Case> cases;

    public void setCases(Case[] tab) throws Exception {
        if (tab == null)
            throw new Exception("Paramètre nul");
        if (this.cases != null)
            throw new Exception("Violation des droits, les cases ne peuvent être modifiées.");
        int size = tab.length;
        for (int i = 0; i < size; i++) {
            this.cases.add(tab[i]);
        }
    }

    public void setEtatCase(int index, EtatCase e)throws Exception{
        this.cases.get(index).setEtat(e);
    }

    public void setValueCase(int index, int valeur)throws Exception{
        this.cases.get(index).setValeur(valeur);
    }

    private ArrayList<Case> getCases() {
        return this.cases;
    }

    public Case [] getAllCases(){
        Case [] tabCases = new Case[this.getCases().size()];
        int size = tabCases.length;
        for(int i = 0; i < size; i++){
            tabCases[i] = this.getCases().get(i);
        }return tabCases;
    }

    public Case getUniqueCaseByIndex(int index) {
        return this.getCases().get(index);
    }

    public Case getUniqueCaseByName(String name) throws Exception {
        boolean found = false;
        int size = this.getCases().size();
        for (int i = 0; i < size; i++) {
            Case c = this.getCases().get(i);
            if (c.getNom().compareToIgnoreCase(name) == 0)
                return c;
        }
        if (found == false)
            throw new Exception("La case n'existe pas : " + name);
        return null;
    }

    @Override
    public String toString() {
        String toString = "";
        int size = this.getCases().size();
        for (int i = 0; i < size; i++) {
            Case c = this.getCases().get(i);
            toString += c.getNom() + " : " + c.getValeur() + "\n";
        }
        return toString;
    }
}
