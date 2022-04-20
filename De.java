import java.util.Random;

/**
 * Dé de jeu de yams. <BR>
 * 
 * @author : Hugo CASTELL
 * 
 */
public class De {
    private int face;
    private EtatDe etat;

    

    public De(EtatDe etat) {
        this.face = this.actualize();
        this.etat = etat;
    }

    public void setEtat(EtatDe etat) {
        this.etat = etat;
    }

    private void setFace(int face) {
        this.face = face;
    }

    public EtatDe getEtat() {
        return this.etat;
    }

    public int getFace() {
        return this.face;
    }

    /**
     * Actualise la valeur du dé entre 1 et 6.
     * @return int
     */
    public int actualize() {
        Random tmp = new Random();
        this.setFace(tmp.nextInt(6 - 1) + 1);
        return this.getFace();
    }
}
