package app6;

/** @author Ahmed Khoumsi */

/** Cette classe identifie les terminaux reconnus et retournes par
 *  l'analyseur lexical
 */
public class Terminal {

  public String chaine;
  public String type;

  public Terminal(String c, String type) {
    this.chaine = c;
    this.type = type;
  }
}