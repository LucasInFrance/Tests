import java.util.ArrayList;

/**
 * Permet de recherche le nombre d'occurences d'un mot dans une grille de caractères
 */
public class Recherche {
    public static int chercheMot(String botteDeFoin, String aiguille) {
        // paramètres erronés
        if (aiguille == null || botteDeFoin == null) {
            return -1;
        }
        if (botteDeFoin.equals("")) {
            return 0;
        }

        //Création et vérification de la grille
        ArrayList<String> lignes = convertirEnGrille(botteDeFoin);
        if (!estGrilleCarree(lignes)) {
            return -1;
        }
        
        //Aiguille à 1 caractère
        if (aiguille.length() == 1) {
            return compterCaractereSimple(lignes, aiguille.charAt(0));
        }

        String aiguilleInverse=inverserMot(aiguille);
        
        //Aiguille à 2+ caractères
        int cptTotal = 0;
        cptTotal += compterHorizontalement(lignes, aiguille, aiguilleInverse);
        cptTotal += compterVerticalement(lignes, aiguille, aiguilleInverse);
        cptTotal += compterDiagonalementNOSU(lignes, aiguille, aiguilleInverse);
        cptTotal += compterDiagonalementNESO(lignes, aiguille, aiguilleInverse);
        
        return cptTotal;
    }
    
    /**
     * Transforme un String en grille (liste de lignes)
     * @param texte texte à transformer
     * @return la grille sous forme d'une liste de lignes
     */
    private static ArrayList<String> convertirEnGrille(String texte) {
        ArrayList<String> lignes=new ArrayList<String>();
        String[] tableauLignes = texte.split("\n");
        for (int i = 0; i < tableauLignes.length; i++) {
            String ligne=tableauLignes[i];
            lignes.add(ligne);
        }
        return lignes;
    }
    

    /**
     * Vérifie que la grille soit carrée
     * @param lignes liste de lignes de la grille
     * @return true si la grille est carrée, sinon false
     */
    private static boolean estGrilleCarree(ArrayList<String> lignes) {
        if (lignes.isEmpty()) {
            return true;
        }
        int longueurPremiereLigne=lignes.get(0).length();
        for (int i = 0; i < lignes.size(); i = i + 1) {
            String ligne = lignes.get(i);
            if (ligne.length()!=longueurPremiereLigne) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Compte le nombre d'appartions d'un caractère dans une grille
     * @param lignes liste de lignes de la grille
     * @param caractere caractère à compter
     * @return le nombre d'appartions du caractère
     */
    private static int compterCaractereSimple(ArrayList<String> lignes, char caractere) {
        int cpt = 0;
        for (int i = 0; i < lignes.size(); i++) {
            String ligne=lignes.get(i);
            for (int j = 0; j < ligne.length(); j = j+1) {
                char caractereActuel = ligne.charAt(j);
                if (caractereActuel==caractere) {
                    cpt = cpt + 1;
                }
            }
        }
        return cpt;
    }

    /**
     * Inverse un mot
     * @param mot mot à inverser
     * @return le mot inversé
     */
    private static String inverserMot(String mot) {
        String motInverse = "";
        for (int i=mot.length() - 1; i >= 0; i--) {
            char caractere=mot.charAt(i);
            motInverse = motInverse + caractere;
        }
        return motInverse;
    }
    

    /**
     * Compte horizontalement le nombre d'appartions d'un mot dans une grille
     * @param lignes liste de lignes de la grille
     * @param mot mot à compter
     * @return le nombre d'appartions du mot
     */
    private static int compterHorizontalement(ArrayList<String> lignes, String mot, String motInverse) {
        int cpt=0;
        
        for (int i = 0; i < lignes.size(); i++) {

            String ligne = lignes.get(i);
            cpt = cpt+nbOccurences(ligne, mot, motInverse);
        }
        return cpt;
    }
    
    /**
     * Compte verticalement le nombre d'appartions d'un mot dans une grille
     * @param lignes liste de lignes de la grille
     * @param mot mot à compter
     * @return le de fois que le mot apparait
     */
    private static int compterVerticalement(ArrayList<String> lignes, String mot, String motInverse) {
        int cpt = 0;
        int hauteur=lignes.size();
        int largeur = lignes.get(0).length();
        
        for (int col = 0; col < largeur; col = col + 1) {
            String colonne="";

            for (int row = 0; row < hauteur; row++) { //Construction du String de la colonne
                char caractere = lignes.get(row).charAt(col);
                colonne=colonne + caractere; 
            }

            cpt=cpt + nbOccurences(colonne, mot, motInverse);
        }
        return cpt;
    }
    
    /**
     * Compte le nombre d'appartions d'un mot dans les diagonales NordOuest - SudEst
     * @param lignes liste de lignes de la grille
     * @param mot mot à compter
     * @return le nombre d'appartions du mot dans les diagonales
     */
    private static int compterDiagonalementNOSU(ArrayList<String> lignes, String mot, String motInverse) {
        int cpt=0;
        int hauteur = lignes.size();
        int largeur = lignes.get(0).length();
        
        // Diagonales commençant de la première ligne (Nord)
        for (int col = 0; col < largeur; col++) {
            String diagonale="";
            int i = 0;
            int j=col;
            while (i < hauteur && j < largeur) {
                char caractere = lignes.get(i).charAt(j);
                diagonale = diagonale + caractere;
                i++;
                j = j + 1;
            }
            if (diagonale.length()>=mot.length()) {
                cpt = cpt + nbOccurences(diagonale, mot, motInverse);
            }
        }
        
        // Diagonales commençant de la première colonne (Ouest) (sauf coin déjà traité)
        for (int row = 1; row < hauteur; row = row + 1) {
            String diagonale="";
            int i = row;
            int j = 0;
            while (i < hauteur && j < largeur) {
                char caractere=lignes.get(i).charAt(j);
                diagonale = diagonale + caractere;
                i=i + 1;
                j++;
            }
            if (diagonale.length()>=mot.length()) {
                cpt=cpt + nbOccurences(diagonale, mot, motInverse);
            }
        }
        
        return cpt;
    }
    

    /**
     * Compte le nombre d'appartions d'un mot dans les diagonales NordEst - SudOuest
     * @param lignes liste de lignes de la grille
     * @param mot mot à compter
     * @return le nombre d'appartions du mot dans les diagonales
     */
    private static int compterDiagonalementNESO(ArrayList<String> lignes, String mot, String motInverse) {
        int cpt = 0;
        int hauteur=lignes.size();
        int largeur = lignes.get(0).length();


        // Diagonales commençant de la dernière ligne (Sud)
        for (int col = 0; col < largeur; col = col + 1) {
            String diagonale="";
            int i = hauteur - 1;
            int j = col;
            while (i >= 0 && j < largeur) {
                char caractere=lignes.get(i).charAt(j);
                diagonale = diagonale + caractere;
                i--;
                j = j + 1;
            }
            if (diagonale.length()>=mot.length()) {
                cpt = cpt + nbOccurences(diagonale, mot, motInverse);
            }
        }
        
        // Diagonales commençant de la première colonne (Est)(sauf coin déjà traité)
        for (int row=hauteur - 2; row >= 0; row--) {
            String diagonale="";
            int i=row;
            int j = 0;
            while (i >= 0 && j < largeur) {
                char caractere = lignes.get(i).charAt(j);
                diagonale = diagonale + caractere;
                i=i - 1;
                j++;
            }
            if (diagonale.length()>=mot.length()) {
                cpt=cpt + nbOccurences(diagonale, mot, motInverse);
            }
        }
        
        return cpt;
    }

    /**
     * Compte le nombre d'appartions d'un mot et son inverse dans un texte
     * @param texte texte a analyser
     * @param mot mot à compter
     * @param motInverse mot inversé
     * @return le nombre d'appartions du mot et de son inverse
     * 
     */
    private static int nbOccurences(String texte, String mot, String motInverse) {
        int cpt=0;

        cpt=cpt + nbMot(texte, mot);
        if (!mot.equals(motInverse)) {
            cpt = cpt + nbMot(texte, motInverse);
        }
        return cpt;
    }
    
    /**
     * Compte le nombre d'appartions d'un mot dans un texte, dans un sens donné
     * @param texte texte a analyser
     * @param mot mot à compter
     * @return le nombre d'appartions du mot
     */
    private static int nbMot(String texte, String mot) {
        int cpt = 0;
        
        for (int i = 0; i <= texte.length() - mot.length(); i++) {
            boolean trouve=true;
            for (int j = 0; j < mot.length(); j = j + 1) {
                char caractereTexte=texte.charAt(i + j);
                char caractereMot=mot.charAt(j);
                if (caractereTexte != caractereMot) {
                    trouve = false;
                    break;
                }
            }
            if (trouve) {
                cpt++;
            }
        }
        
        return cpt;
    }
}
