package app6;

/** @author Ahmed Khoumsi */

/** Classe representant une feuille d'AST
 */
public class FeuilleAST extends ElemAST {

    private String operande;


/**Constructeur pour l'initialisation d'attribut(s)
 */
    public FeuilleAST(String c) {
        this.operande = c;
    }


  /** Evaluation de feuille d'AST
   */
  public int EvalAST( ) {
      if (Character.isDigit(this.operande.charAt(0))) {
          return Integer.parseInt(this.operande);
      }
      else {
          return 0;
      }
  }


    /**
     * Lecture de chaine de caracteres correspondant a la feuille d'AST
     */
    public String LectAST() {
        return this.operande;
  }

}
