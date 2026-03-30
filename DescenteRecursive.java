package app6;

/** @author Ahmed Khoumsi */

/** Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

  private AnalLex lexical;
  private Terminal tCourant;
  private String sequenceLue = "";

/** Constructeur de DescenteRecursive :
      - recoit en argument le nom du fichier contenant l'expression a analyser
      - pour l'initalisation d'attribut(s)
 */

public DescenteRecursive(String in) {
  Reader r = new Reader(in);
  this.lexical = new AnalLex(r.toString());
}

private void avancerTerminal() {
  if (this.tCourant != null) {
    this.sequenceLue += this.tCourant.chaine + " ";
  }
  this.tCourant = this.lexical.prochainTerminal();
}

/** AnalSynt() effectue l'analyse syntaxique et construit l'AST.
 *    Elle retourne une reference sur la racine de l'AST construit
 */
public ElemAST AnalSynt( ) {

  this.tCourant = this.lexical.prochainTerminal();
  ElemAST racine = this.E();

  if (this.tCourant != null) {
    this.ErreurSynt("Symbole inattendu à la fin de l'expression.", this.tCourant.chaine, "Fin de l'expression");
  }
  return racine;
}

  public ElemAST E() {
    ElemAST noeud1 = T(); // On lit toujours un T en premier

    // Si on observe un + ou un -
    if (this.tCourant != null && (this.tCourant.chaine.equals("+") || this.tCourant.chaine.equals("-"))) {
      String operateur = this.tCourant.chaine;
      this.avancerTerminal();

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
      this.avancerTerminal();

      ElemAST noeud2 = T(); // Appel récursif
      return new NoeudAST(operateur, noeud1, noeud2);
    }

    return noeud1;
  }

  public ElemAST F() {
    if (this.tCourant == null) {
      this.ErreurSynt("Opérande ou parenthèse manquante.", "Fin de fichier", "Nombre, Variable ou '('");
      return null;
    }

    // Cas 1 : ( E )
    if (this.tCourant.chaine.equals("(")) {
      this.avancerTerminal(); // On mange la '('

      ElemAST noeud = E(); // On analyse tout ce qui est à l'intérieur

      // On s'attend obligatoirement à refermer la parenthèse
      if (this.tCourant != null && this.tCourant.chaine.equals(")")) {
        this.avancerTerminal(); // On mange la ')'
        return noeud; // On retourne le noeud intérieur (les parenthèses disparaissent de l'AST !)
      } else {
        String recu = (this.tCourant != null) ? this.tCourant.chaine : "Fin de fichier";
        this.ErreurSynt("Parenthèse fermante manquante.", recu, "')'");
        return null;
      }
    }

    else if (this.tCourant.type.equals("Nombre") || this.tCourant.type.equals("Identificateur")) {
      ElemAST feuille = new FeuilleAST(this.tCourant.chaine);
      this.avancerTerminal();
      return feuille;
    }
    else {
      this.ErreurSynt("Unité lexicale non permise ici.", this.tCourant.chaine, "Nombre, Variable ou '('");
      return null;
    }
  }

/** ErreurSynt() envoie un message d'erreur syntaxique
 */
public void ErreurSynt(String cause, String recu, String attendu) {
  System.out.println("\n*** ERREUR SYNTAXIQUE ***");
  System.out.println("Lieu : juste après la séquence d'unités lexicales \"" + this.sequenceLue.trim() + "\"");
  System.out.println("Cause : " + cause);
  System.out.println("Unité lexicale interdite reçue : '" + recu + "'");
  System.out.println("Unités lexicales permises : " + attendu);
  System.exit(1);
}

  //Methode principale a lancer pour tester l'analyseur syntaxique.
  public static void main(String[] args) {
    String toWriteLect = "";
    String toWriteEval = "";

    System.out.println("Debut d'analyse syntaxique");

    try {
      java.io.FileWriter fw = new java.io.FileWriter("ExpArith.txt");
      fw.write("(U_x-V_y)*W_z/35");
      fw.write("(55-47)*14/2");
      fw.write("(u_x-)*W_z/35");
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

