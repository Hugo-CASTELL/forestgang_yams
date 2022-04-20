public class CaseBonus extends Case{
    private int bonus;
    private int condition;

    public CaseBonus(String nom, EtatCase etat, int bonus, int condition) {
        super(nom, etat);
        this.bonus = bonus;
        this.condition = condition;
    }

    @Override
    public void setEtat(EtatCase etat) {
        if(etat != EtatCase.BARREE) super.setEtat(etat);
    };

    public int getBonus() {
        return bonus;
    }
    public int getCondition() {
        return condition;
    }
}
