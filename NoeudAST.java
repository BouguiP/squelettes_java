package app6;

/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class NoeudAST extends ElemAST {

  private String operateur;
  private ElemAST enfantGauche;
  private ElemAST enfantDroit;

  /** Constructeur pour l'initialisation d'attributs
   */
  public NoeudAST(String op, ElemAST gauche, ElemAST droit) {
    this.operateur = op;
    this.enfantGauche = gauche;
    this.enfantDroit = droit;
  }

 
  /** Evaluation de noeud d'AST
   */
  public int EvalAST() {
    int valGauche = this.enfantGauche.EvalAST();
    int valDroite = this.enfantDroit.EvalAST();

    if (this.operateur.equals("+")) {
      return valGauche + valDroite;
    } else if (this.operateur.equals("-")) {
      return valGauche - valDroite;
    } else if (this.operateur.equals("*")) {
      return valGauche * valDroite;
    } else if (this.operateur.equals("/")) {
      return valGauche / valDroite;
    } else {
      // Au cas où un opérateur bizarre se glisse dans notre arbre
      this.ErreurEvalAST("Opérateur inconnu : " + this.operateur);
      return 0;
    }
  }


  /** Lecture de noeud d'AST
   */
  public String LectAST() {
    return "(" + this.enfantGauche.LectAST() + this.operateur + this.enfantDroit.LectAST() + ")";
  }

}


