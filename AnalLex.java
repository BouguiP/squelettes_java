package app6;

/** @author Ahmed Khoumsi */

/** Cette classe effectue l'analyse lexicale
 */
public class AnalLex {

  private String expression;
  private int position;

	
/** Constructeur pour l'initialisation d'attribut(s)
 */
  public AnalLex(String in) {

    this.expression = in;
    this.position = 0;
}


/** resteTerminal() retourne :
      false  si tous les terminaux de l'expression arithmetique ont ete retournes
      true s'il reste encore au moins un terminal qui n'a pas ete retourne 
 */
  public boolean resteTerminal( ) {

    return this.position < this.expression.length();
  }
  
  
/** prochainTerminal() retourne le prochain terminal
      Cette methode est une implementation d'un AEF
 */
public Terminal prochainTerminal() {

  if (!this.resteTerminal()) {
    return null;
  }

  while (this.resteTerminal() && this.expression.charAt(this.position) == ' ') {
    this.position++;
  }

  if (!this.resteTerminal()) {
    return null;
  }
  char c = this.expression.charAt(this.position);

  if (c == '+') { position++; return new Terminal("+", "Addition"); }
  if (c == '-') { position++; return new Terminal("-", "Soustraction"); }
  if (c == '*') { position++; return new Terminal("*", "Multiplication"); }
  if (c == '/') { position++; return new Terminal("/", "Division"); }
  if (c == '(') { position++; return new Terminal("(", "ParentheseOuvrante"); }
  if (c == ')') { position++; return new Terminal(")", "ParentheseFermante"); }


  if (Character.isDigit(c)) {
    String resultat = "";
    while (this.resteTerminal() && Character.isDigit(this.expression.charAt(position))) {
      resultat += this.expression.charAt(position);
      position++;
    }
    return new Terminal(resultat,"Nombre");
  }

  if (Character.isUpperCase(c)) {
    String resultat = "";

    while (this.resteTerminal()) {
      char courant = this.expression.charAt(this.position);

      if (Character.isLetter(courant)) {
        resultat += courant;
        this.position++;
      }

      else if (courant == '_') {
        resultat += courant;
        this.position++;

        if (this.resteTerminal() && Character.isLetter(this.expression.charAt(this.position))) {
          resultat += this.expression.charAt(this.position);
          this.position++;
        } else {

          char recu = this.resteTerminal() ? this.expression.charAt(this.position) : ' ';
          this.ErreurLex("Un tiret bas '_' doit être suivi d'une lettre.", "Lettre (a-z, A-Z)", recu);
          return null;
        }
      }
      else {
        break;
      }
    }
    return new Terminal(resultat,"Identificateur");
  }

  this.ErreurLex("Caractère non reconnu.", "Chiffre, Majuscule, Opérateur ou Parenthèse", c);
  return null;

}

 
/** ErreurLex() envoie un message d'erreur lexicale
 */
public void ErreurLex(String cause, String attendu, char recu) {
  System.out.println("\n*** ERREUR LEXICALE ***");
  String sequence = this.expression.substring(0, this.position);
  System.out.println("Lieu : juste après la séquence \"" + sequence + "\"");
  System.out.println("Cause : " + cause);
  System.out.println("Caractère interdit reçu : '" + (recu == ' ' ? "Fin de fichier" : recu) + "'");
  System.out.println("Caractères permis ici : " + attendu);
  System.exit(1);
}

  
  //Methode principale a lancer pour tester l'analyseur lexical
  public static void main(String[] args) {
    String toWrite = "";
    System.out.println("Debut d'analyse lexicale");
    if (args.length == 0){
    args = new String [2];
            args[0] = "ExpArith.txt";
            args[1] = "ResultatLexical.txt";
    }
    Reader r = new Reader(args[0]);

    AnalLex lexical = new AnalLex(r.toString()); // Creation de l'analyseur lexical

    // Execution de l'analyseur lexical
    Terminal t = null;
    while(lexical.resteTerminal()){
      t = lexical.prochainTerminal();
      toWrite +=t.chaine + "\n" ;  // toWrite contient le resultat
    }				   //    d'analyse lexicale
    System.out.println(toWrite); 	// Ecriture de toWrite sur la console
    Writer w = new Writer(args[1],toWrite); // Ecriture de toWrite dans fichier args[1]
    System.out.println("Fin d'analyse lexicale");
  }
}
