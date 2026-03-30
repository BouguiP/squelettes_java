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

  if (c == '+') { this.position++; return new Terminal("+", "Addition"); }
  if (c == '-') { this.position++; return new Terminal("-", "Soustraction"); }
  if (c == '*') { this.position++; return new Terminal("*", "Multiplication"); }
  if (c == '/') { this.position++; return new Terminal("/", "Division"); }
  if (c == '(') { this.position++; return new Terminal("(", "ParentheseOuvrante"); }
  if (c == ')') { this.position++; return new Terminal(")", "ParentheseFermante"); }


  if (Character.isDigit(c)) {
    String resultat = "";
    while (this.resteTerminal() && Character.isDigit(this.expression.charAt(this.position))) {
      resultat += this.expression.charAt(this.position);
      this.position++;
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


  
  public static void main(String[] args) {

    String[] tests = {
            "(U_x+V_y)*W_z/35",
            "(U_x+V_y)*W__z/35"
    };

    for (int i = 0; i < tests.length; i++) {
      System.out.println("\n--- Expression arithmétique : " + tests[i] + " ---");
      System.out.println("Unités lexicales retournées :");
      AnalLex lexical = new AnalLex(tests[i]);
      while(lexical.resteTerminal()){
        Terminal t = lexical.prochainTerminal();
        if (t != null) {
          System.out.println(t.type + " " + t.chaine);
        }
      }
    }
  }
}
