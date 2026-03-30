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

  if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')') {
    position++;
    return new Terminal(String.valueOf(c));
  }

  if (Character.isDigit(c)) {
    String resultat = "";
    while (this.resteTerminal() && Character.isDigit(this.expression.charAt(position))) {
      resultat += this.expression.charAt(position);
      position++;
    }
    return new Terminal(resultat);
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

          this.ErreurLex("Erreur lexicale : un '_' doit obligatoirement être suivi d'une lettre.");
          return null;
        }
      }
      else {
        break;
      }
    }
    return new Terminal(resultat);
  }

  ErreurLex("Erreur lexicale : caractère non reconnu '" + c + "' à la position " + this.position);
  return null;

}

 
/** ErreurLex() envoie un message d'erreur lexicale
 */ 
  public void ErreurLex(String s) {
    System.out.println(s);
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
