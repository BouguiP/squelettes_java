package app6;

/** @author Ahmed Khoumsi */

/** Cette classe effectue l'analyse syntaxique
 */
public class DescenteRecursive {

  private AnalLex lexical;

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
  return this.E();
}

public ElemAST E() {
    // On commence toujours par lire un T
    ElemAST noeud1 = T();

    // On regarde le prochain symbole
    Terminal t = this.lexical.prochainTerminal();

    // Si on a un symbole et que c'est un "+"
    if (t != null && t.chaine.equals("+")) {
      ElemAST noeud2 = E();
      return new NoeudAST(t.chaine, noeud1, noeud2);
    }
    // Cas 2 : Il n'y a plus de symbole (fin de l'expression, t est null)
    else if (t == null) {
      return noeud1;
    }
    // Cas 3 : Symbole inattendu (erreur de syntaxe)
    else {
      this.ErreurSynt("Erreur syntaxique : '+' attendu, mais '" + t.chaine + "' trouvé.");
      return null; // Requis pour la compilation
    }
}

public ElemAST T() {
    // 1. On demande à l'analyseur de lire le prochain morceau (notre nombre "a")
    Terminal t = this.lexical.prochainTerminal();

    // 2. On utilise le texte de ce terminal pour créer et retourner la feuille
    return new FeuilleAST(t.chaine);
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
      fw.write("101+71"); // Tu peux changer l'expression ici pour tester
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
    System.out.println("Analyse syntaxique terminee");
  }

}

