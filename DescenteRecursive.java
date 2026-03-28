package app6;

/** @author Ahmed Khoumsi */

/** Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

  private AnalLex lexical;
  private Terminal tCourant;

/** Constructeur de DescenteRecursive :
      - recoit en argument le nom du fichier contenant l'expression a analyser
      - pour l'initalisation d'attribut(s)
 */
public DescenteRecursive(String in) {
  Reader r = new Reader(in);
  this.lexical = new AnalLex(r.toString());
}


/** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
 *    Elle retourne une reference sur la racine de l'AST construit
 */
public ElemAST AnalSynt( ) {

  this.tCourant = this.lexical.prochainTerminal();
  ElemAST racine = this.E();

  if (this.tCourant != null) {
    this.ErreurSynt("Erreur syntaxique : symboles inattendus à la fin de l'expression.");
  }
  return racine;
}

  public ElemAST E() {
    ElemAST noeud1 = T(); // On lit toujours un T en premier

    // Si on observe un + ou un -
    if (this.tCourant != null && (this.tCourant.chaine.equals("+") || this.tCourant.chaine.equals("-"))) {
      String operateur = this.tCourant.chaine;
      this.tCourant = this.lexical.prochainTerminal(); // On avance le curseur (on mange l'opérateur)

      ElemAST noeud2 = E(); // Appel récursif pour l'associativité à droite
      return new NoeudAST(operateur, noeud1, noeud2);
    }

    // Sinon, c'était juste un T tout seul
    return noeud1;
  }

  public ElemAST T() {
    ElemAST noeud1 = F(); // On lit toujours un F en premier

    // Si on observe un * ou un /
    if (this.tCourant != null && (this.tCourant.chaine.equals("*") || this.tCourant.chaine.equals("/"))) {
      String operateur = this.tCourant.chaine;
      this.tCourant = this.lexical.prochainTerminal(); // On avance le curseur

      ElemAST noeud2 = T(); // Appel récursif
      return new NoeudAST(operateur, noeud1, noeud2);
    }

    return noeud1;
  }

  public ElemAST F() {
    if (this.tCourant == null) {
      this.ErreurSynt("Erreur syntaxique : opérande ou parenthèse manquante.");
      return null;
    }

    // Cas 1 : ( E )
    if (this.tCourant.chaine.equals("(")) {
      this.tCourant = this.lexical.prochainTerminal(); // On mange la '('

      ElemAST noeud = E(); // On analyse tout ce qui est à l'intérieur

      // On s'attend obligatoirement à refermer la parenthèse
      if (this.tCourant != null && this.tCourant.chaine.equals(")")) {
        this.tCourant = this.lexical.prochainTerminal(); // On mange la ')'
        return noeud; // On retourne le noeud intérieur (les parenthèses disparaissent de l'AST !)
      } else {
        this.ErreurSynt("Erreur syntaxique : parenthèse fermante ')' manquante.");
        return null;
      }
    }
    // Cas 2 : a (Un nombre ou un identifiant/variable)
    else {
      ElemAST feuille = new FeuilleAST(this.tCourant.chaine);
      this.tCourant = this.lexical.prochainTerminal();
      return feuille;
    }
  }

/** ErreurSynt() envoie un message d'erreur syntaxique
 */
public void ErreurSynt(String s)
{
  System.out.println(s);
  System.exit(1);
}



  //Methode principale a lancer pour tester l'analyseur syntaxique.
  public static void main(String[] args) {
    String toWriteLect = "";
    String toWriteEval = "";

    System.out.println("Debut d'analyse syntaxique");

    try {
      java.io.FileWriter fw = new java.io.FileWriter("ExpArith.txt");
      fw.write("(2+8)*9/2"); // Tu peux changer l'expression ici pour tester
      fw.close();
    } catch (Exception e) {
      System.out.println("Impossible de créer le fichier de test.");
    }

    if (args.length == 0){
      args = new String [2];
      args[0] = "ExpArith.txt";
      args[1] = "ResultatSyntaxique.txt";
    }
    DescenteRecursive dr = new DescenteRecursive(args[0]);
    try {
      ElemAST RacineAST = dr.AnalSynt();
      toWriteLect += "Lecture de l'AST trouve : " + RacineAST.LectAST() + "\n";
      System.out.println(toWriteLect);
      toWriteEval += "Evaluation de l'AST trouve : " + RacineAST.EvalAST() + "\n";
      System.out.println(toWriteEval);
      Writer w = new Writer(args[1],toWriteLect+toWriteEval);

    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
      System.exit(51);
    }
    System.out.println("Analyse syntaxique terminée");
  }

}

