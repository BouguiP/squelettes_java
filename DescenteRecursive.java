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
      this.ErreurSynt("Unité lexicale non permise ici.", this.tCourant.chaine, "Nombre, Variable");
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

  // Methode principale a lancer pour tester l'analyseur syntaxique (VALIDATION PDF).
  public static void main(String[] args) {
    // Les 3 tests demandés par le professeur dans validation_am.pdf
    String[] tests = {
            "(U_x-V_y)*W_z/35",     // 1. AST sans erreur (avec variables)
            "(55-47)*14/2",         // 2. AST sans erreur (nombres purs, évaluation possible)
            "(U_x-)*W_z/35"         // 3. Erreur syntaxique attendue (on simule l'erreur du PDF)
    };

    for (int i = 0; i < tests.length; i++) {
      System.out.println("\n\n--- ANALYSE DE L'EXPRESSION " + (i+1) + " : " + tests[i] + " ---");

      // 1. On écrit l'expression dans le fichier
      try {
        java.io.FileWriter fw = new java.io.FileWriter("ExpArith.txt");
        fw.write(tests[i]);
        fw.close();
      } catch (Exception e) {
        System.out.println("Impossible de créer le fichier de test.");
      }

      // 2. On lance l'analyse
      DescenteRecursive dr = new DescenteRecursive("ExpArith.txt");
      try {
        ElemAST arbre = dr.AnalSynt();
        System.out.println("- Lecture de l'AST construit : " + arbre.LectAST());
        System.out.println("- Expression postfix de l'AST construit : " + arbre.Postfix());

        try {
          System.out.println("- Évaluation de l'AST construit : " + arbre.EvalAST());
        } catch (UnsupportedOperationException e) {
          System.out.println("- Évaluation de l'AST construit : \n  -> " + e.getMessage());
        }
      } catch (Exception e) {
        // Le programme s'arrête de lui-même avec System.exit(1) dans ErreurSynt ou ErreurLex
      }
    }
  }

}

