package iut.sae.algo.simplicite;

import java.util.ArrayList;

/**
 * Permet de recherche le nombre d'occurences d'un mot dans une grille de caractères
 */
public class Recherche {
    public static int chercheMot(String botteDeFoin, String aiguille) {
        // paramètres erronés
        if (botteDeFoin == null || aiguille == null) {
            return -1;
        }
        if (botteDeFoin.equals("") || aiguille.equals("")) {
            return 0;
        }

        //Création de la grille (liste de lignes) et Vérification
        ArrayList<String> grille = convertirEnGrille(botteDeFoin);
        if (!estGrilleReguliere(grille)) {
            return -1;
        }
        
        //Aiguille à 1 caractère
        if (aiguille.length() == 1) {
            return compterAiguilleSimple(grille, aiguille.charAt(0));
        }

        String aiguilleInverse=inverserMot(aiguille);
        
        //Aiguille à 2+ caractères
        int cptTotal = 0;
        cptTotal += compterHorizontalement(grille, aiguille, aiguilleInverse);
        cptTotal += compterVerticalement(grille, aiguille, aiguilleInverse);
        cptTotal += compterDiagonalementNOSU(grille, aiguille, aiguilleInverse);
        cptTotal += compterDiagonalementNESO(grille, aiguille, aiguilleInverse);
        
        return cptTotal;
    }
    
    /**
     * Transforme un String en grille (liste de lignes)
     * @param texte texte à transformer
     * @return la grille sous forme d'une liste de lignes
     */
    private static ArrayList<String> convertirEnGrille(String texte) {

        ArrayList<String> grille=new ArrayList<String>();
        String[] tableauLignes = texte.split("\n");
        for (int i = 0; i < tableauLignes.length; i++) {
            String ligne=tableauLignes[i];
            grille.add(ligne);
        }
        return grille;
    }
    

    /**
     * Vérifie que la grille soit régulière
     * @param grille liste de lignes de la grille
     * @return true si la grille est régulière, sinon false
     */
    private static boolean estGrilleReguliere(ArrayList<String> grille) {

        int longueurPremiereLigne=grille.get(0).length();
        for (int i = 0; i < grille.size(); i = i + 1) {
            String ligne = grille.get(i);
            if (ligne.length()!=longueurPremiereLigne) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Compte le nombre d'appartions d'une aiguille d'un caractère dans une grille
     * @param grille liste de lignes de la grille
     * @param caractere caractère à compter
     * @return le nombre d'appartions de l'aiguille
     */
    private static int compterAiguilleSimple(ArrayList<String> grille, char caractere) {
        int cpt = 0;
        for (int i = 0; i < grille.size(); i++) {
            String ligne=grille.get(i);
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
     * @param grille liste de lignes de la grille
     * @param mot mot à compter
     * @return le nombre d'appartions du mot
     */
    private static int compterHorizontalement(ArrayList<String> grille, String mot, String motInverse) {
        int cpt=0;
        
        for (int i = 0; i < grille.size(); i++) {// pour chaque ligne

            String ligne = grille.get(i);
            cpt = cpt+nbOccurenceMot(ligne, mot, motInverse);
        }
        return cpt;
    }
    
    /**
     * Compte verticalement le nombre d'appartions d'un mot dans une grille
     * @param grille liste de lignes de la grille
     * @param mot mot à compter
     * @return le de fois que le mot apparait
     */
    private static int compterVerticalement(ArrayList<String> grille, String mot, String motInverse) {
        int cpt = 0;
        int hauteur=grille.size();
        int largeur = grille.get(0).length();
        
        for (int col = 0; col < largeur; col = col + 1) {// pour chaque colonne
            String colonne="";

            for (int row = 0; row < hauteur; row++) { //Construction du String de la colonne
                char caractere = grille.get(row).charAt(col);
                colonne=colonne + caractere; 
            }

            cpt=cpt + nbOccurenceMot(colonne, mot, motInverse);
        }
        return cpt;
    }

    
    /**
     * Compte le nombre d'appartions d'un mot dans les diagonales NordOuest - SudEst
     * @param grille liste de lignes de la grille
     * @param mot mot à compter
     * @return le nombre d'appartions du mot dans les diagonales
     */
    private static int compterDiagonalementNOSU(ArrayList<String> grille, String mot, String motInverse) {
        int cpt=0;
        int hauteur = grille.size();
        int largeur = grille.get(0).length();
        
        // Diagonales commençant de la première ligne (Nord)
        for (int col = 0; col < largeur; col++) {
            cpt = cpt + compterLaDiagonale(grille, 0, col, 1, 1, mot, motInverse);
        }
        
        // Diagonales commençant de la première colonne (Ouest) (sauf coin déjà traité)
        for (int row = 1; row < hauteur; row = row + 1) {
            cpt = cpt + compterLaDiagonale(grille, row, 0, 1, 1, mot, motInverse);
        }
        
        return cpt;
    }
    

    /**
     * Compte le nombre d'appartions d'un mot dans les diagonales NordEst - SudOuest
     * @param grille liste de lignes de la grille
     * @param mot mot à compter
     * @return le nombre d'appartions du mot dans les diagonales
     */
    private static int compterDiagonalementNESO(ArrayList<String> grille, String mot, String motInverse) {
        int cpt = 0;
        int hauteur=grille.size();
        int largeur = grille.get(0).length();

        // Diagonales commençant de la dernière ligne (Sud)
        for (int col = 0; col < largeur; col++) {
            cpt = cpt + compterLaDiagonale(grille, hauteur - 1, col, -1, 1, mot, motInverse);
        }
        
        // Diagonales commençant de la première colonne (Est)(sauf coin déjà traité)
        for (int row=hauteur - 2; row >= 0; row--) {
            cpt = cpt + compterLaDiagonale(grille, row, 0, -1, 1, mot, motInverse);
        }
        
        return cpt;
    }

    /**
     * Compte les occurences d'un mot dans une diagonale de caractères
     * @param grille liste de lignes de la grille
     * @param debutRow ligne de départ
     * @param debutCol colonne de départ
     * @param incrRow incrément de ligne (1 pour descendre, -1 pour monter)
     * @param incrCol incrément de colonne (1 pour aller à droite, -1 pour aller à gauche)
     * @param mot le mot à compter
     * @param motInverse le mot inversé
     * @return le nombre d'occurrences trouvées
     */
    private static int compterLaDiagonale(ArrayList<String> grille, int debutRow, int debutCol, int incrRow, int incrCol, String mot, String motInverse) {
        String diagonale = construireStringDiagonale(grille, debutRow, debutCol, incrRow, incrCol);
        if (diagonale.length() >= mot.length()) {
            return nbOccurenceMot(diagonale, mot, motInverse);
        }
        return 0;
    }

    /**
     * Construit un String à partir d'une diagonale de caractères
     * @param grille liste de lignes de la grille
     * @param debutRow ligne de départ
     * @param debutCol colonne de départ
     * @param incrRow incrément de ligne (1 pour descendre, -1 pour monter)
     * @param incrCol incrément de colonne (1 pour aller à droite, -1 pour aller à gauche)
     * @return la diagonale sous forme de String
     * 
     */
    private static String construireStringDiagonale(ArrayList<String> grille, int debutRow, int debutCol, int incrRow, int incrCol) {
        String diagonale = "";
        int hauteur= grille.size();
        int largeur= grille.get(0).length();
        int i = debutRow;
        int j = debutCol;
        
        while (i >= 0 && i < hauteur && j >= 0 && j < largeur) {
            char caractere = grille.get(i).charAt(j);
            diagonale = diagonale + caractere;
            i = i +incrRow;
            j = j +incrCol;
        }
        
        return diagonale;
    }

    /**
     * Compte le nombre d'appartions d'un mot et son inverse dans un texte
     * @param texte texte a analyser
     * @param mot mot à compter
     * @param motInverse mot inversé
     * @return le nombre d'appartions du mot et de son inverse
     * 
     */
    private static int nbOccurenceMot(String texte, String mot, String motInverse) {
        int cpt=0;

        cpt=cpt + nbMot(texte, mot);
        if (!mot.equals(motInverse)) {
            cpt = cpt + nbMot(texte,motInverse);
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
        
        for (int i =0; i <= texte.length() -mot.length(); i++) {
            boolean trouve=true;
            for (int j= 0; j < mot.length(); j = j + 1) {
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
