import java.util.Random;
import java.util.Scanner;

public class Main {

    /**
     * Il gioco inizia chiedendo al giocatore che versione del tris vuole giocare tra quello classico, il 5x5 e l'Ultimate tris.
     * Una volta scelta la versione chiede al giocatore se conosce le regole, se non le conosce stampa le regole per la versione che ha scelto.
     * In base alla versione scelta il programma stabilisce la grandezza del campo di gioco.
     * Poi il programma chiede all'utente se vuole giocare contro un altro giocatore o contro il computer.
     * Infine in base alla versione del gioco selezionata chiama il metodo per giocare opportuno.
     * @param args 167
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rand = new Random();

        int vers = scegliVersione(sc);

        spiegaRegole(sc, vers);

        int n = scegliDimensione(vers);

        char[][][] ultimateMatrix = new char[9][3][3];
        char[][] matrix = new char[n][n];
        // Creazione della matrice di gioco vuota
        if (vers == 3) {
            generaUltimateMatrix(ultimateMatrix);
            generaCampo(matrix);
        } else {
            generaCampo(matrix);
        }

        int mod = scegliMod(sc);
        int diff = 0;
        if (mod == 2) {
            do {
            System.out.println("Scegli la difficoltà");
            System.out.println("1. Stupido");
            System.out.println("2. 'Normale'");
            diff = sc.nextInt();
            if (diff <0 || diff > 2) {
                System.out.println("Scelta non valida");
            }
            } while (diff<0 || diff>2);
        }

        if (vers == 3) {
            printUltimateMatrix(ultimateMatrix);
        } else {
            printMatrix(matrix);
        }

        int turno = 1;
        boolean vinto = false;

        if (vers == 3) {
            giocaUltimateMatrix(vinto, ultimateMatrix, turno, mod, rand, sc, matrix, diff);
        } else {
            gioca(vinto, matrix, vers, turno, mod, rand, sc, diff);
        }

    }

    /**
     * Metodo per la versione 3
     * Il metodo controlla se una macrocella è stata vinta utilizzando il metodo checkVittoria. NON CONTROLLA TUTTO IL CAMPO, SOLO UNA MACROCELLA
     * @param ultimateMatrix campo di gioco  per l'ultimate tris
     * @param macrocella macrocella selezionata
     * @param turno variabile che gestisce i turni, qua utilizzata per il metodo getSimboloGiocatore, quindi per stabilire che simbolo stampare
     * @return true se qualcuno ha vinto, false se ancora nessuno ha vinto
     */
    public static boolean checkWinMacroCella(char[][][] ultimateMatrix, int macrocella, int turno) {
        if (checkVittoria(ultimateMatrix[macrocella])) {
            System.out.println("Macrocella " + (macrocella + 1) + " vinta dal giocatore " + getSimboloGiocatore(turno - 1));
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    ultimateMatrix[macrocella][i][j] = getSimboloGiocatore(turno - 1);
                }
            }
            printUltimateMatrix(ultimateMatrix);
            return true;
        } else {
            System.out.println("Macrocella non ancora vinta");
            return false;
        }
    }

    /**
     * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
     * #                                                                                                                                                                                           #
     * #  Metodo che gestisce il gioco. Si inizia facendo scegliere al primo giocatore in che macrocella vuole far iniziare il gioco, se non è compresa tra 1 e 9 la fa riinserire.                #
     * #  Si entra in un while solo se nessuno ha vinto e se non si è al turno 81 (quindi è ancora possibile giocare) controlla se una macrocella è -1 (quindi se nessuna macrocella è indicata    #
     * #  al momento), se nessuna macrocella è indicata la fa inserire al giocatore che è di turno potendo scegliere un numero tra 1 e 9 e controllando se è occupata o no.                        #
     * #  Il gioco comunica al giocatore in che macrocella posizionerà il suo simbolo. Fa inserire al giocatore le coordinate in cui vuole inserire il simbolo controllando se x e y sono          #
     * #  compresi tra 1 e 3 con il metodo getValidNumber. Controlla se la casella inserita è libera quindi uguale a [#], se lo è, la sostituisce con il simbolo del giocatore che sta giocando al #
     * #  momento, aumenta turno di 1 e ristampa il campo di gioco.
     * #
     * # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # # #
     * @param vinto Variabile booleana per capire se qualcuno ha vinto o no
     * @param ultimateMatrix Campo di gioco per l'ultimate tris
     * @param turno Variabile per gestire il turno, sale di 1 a ogni "giro" e per controllare a chi tocca si verifica se è divisibile per 2 o no. Se è maggiore o uguale al numero delle celle
     *              del campo (81), il gioco finisce.
     * @param mod Variabile che indica la modalità selezionata dall'utente, quindi se si gioca in 2 o contro un pc.
     * @param rand random
     * @param sc scanner
     * @param matrix Matrixe 3x3, in questo caso utilizzata per controllare la vittoria del gioco intero. Quando una macrocella viene vinta, viene segnata in questa matrice.
     */
    public static void giocaUltimateMatrix(boolean vinto, char[][][] ultimateMatrix, int turno, int mod, Random rand, Scanner sc, char[][] matrix, int diff) {
        int macrocella;
        int newmacrocella;

        do {
            System.out.println("Scegliere la macrocella in cui iniziare (1-9)");
            macrocella = sc.nextInt() - 1;
            if (macrocella < 0 || macrocella > 8) {
                System.out.println("Macrocella invalida");
            }
        } while (macrocella < 0 || macrocella > 8);

        while (!vinto && turno <= 81) {
            if (macrocella == -1) {
                if (getSimboloGiocatore(turno-1) == 'X') {
                    do {
                        System.out.println("Scegliere la macrocella in cui giocare (1-9)");
                        macrocella = sc.nextInt();
                        macrocella--;
                        if (!selezionaMacrocella(ultimateMatrix, macrocella)) {
                            System.out.println("Macrocella gia occupata");
                        }
                    } while (!selezionaMacrocella(ultimateMatrix, macrocella));
                } else {
                    do {
                        macrocella = rand.nextInt(9);
                    } while (!selezionaMacrocella(ultimateMatrix, macrocella));
                    System.out.println("Macrocella scelta dal pc "+(macrocella+1));
                }
            }

            System.out.println("Giocherai nella macrocella " + (macrocella + 1));
            int x = -1, y = -1;
            System.out.println("Giocatore " + getSimboloGiocatore(turno));

            if ((turno % 2 == 1 || turno % 2 != 1 && mod == 1) || (turno % 2 == 1 && mod == 2)) {
                x = getValidNumber(sc, "Inserisci la riga:", 3);
                y = getValidNumber(sc, "Inserisci la colonna:", 3);
            } else if (mod == 2 && turno % 2 != 1) {
                if (diff==1) {
                    do {
                        x = rand.nextInt(3);
                        y = rand.nextInt(3);
                    } while (ultimateMatrix[macrocella][x][y] != '#');
                } else {
                    boolean win = false;
                    // controlla se può vincere e fa la mossa
                    for (int i = 0; i < 3 && !win; i++) {
                        for (int j = 0; j < 3; j++) {
                            x=i;
                            y=j;
                            if (ultimateMatrix[macrocella][x][y] == '#') {
                                ultimateMatrix[macrocella][x][y] = 'O';
                                if (checkVittoria(ultimateMatrix[macrocella])) {
                                    ultimateMatrix[macrocella][x][y] = '#';  // Se vince, lascia la mossa e ripristina la casella per non riscontrare errori
                                    win = true;
                                } else {
                                    ultimateMatrix[macrocella][x][y] = '#'; // Ripristina la casella se non va bene la mossa
                                }
                            }
                        }
                    }


                    // Controllo se il giocatore può vincere e lo blocca
                    for (int i = 0; i < 3 && !win; i++) {
                        for (int j = 0; j < 3; j++) {
                            x=i;
                            y=j;
                            if (ultimateMatrix[macrocella][x][y] == '#') {
                                ultimateMatrix[macrocella][x][y] = 'X';
                                if (checkVittoria(ultimateMatrix[macrocella])) {
                                    ultimateMatrix[macrocella][x][y] = '#'; // Blocca il giocatore e ripristina la casella altrimenti riscontra errori
                                    win = true;
                                } else {
                                    ultimateMatrix[macrocella][x][y] = '#'; // Ripristina la casella se non va bene la mossa
                                }
                            }
                        }
                    }

                     // se niente va bene fa una mossa random
                    x = rand.nextInt(3);
                    y = rand.nextInt(3);

                    while (ultimateMatrix[macrocella][x][y] != '#') {
                        x = rand.nextInt(3);
                        y = rand.nextInt(3);
                    }
                    System.out.println("Coordinate scelte dal pc ["+x+","+y+"] alla macrocella "+(macrocella+1));
                }
            }

            if (ultimateMatrix[macrocella][x][y] == '#') {
                ultimateMatrix[macrocella][x][y] = getSimboloGiocatore(turno);
                turno++;
                printUltimateMatrix(ultimateMatrix);

                if (checkWinMacroCella(ultimateMatrix, macrocella, turno)) {
                    matrix[macrocella / 3][macrocella % 3] = getSimboloGiocatore(turno - 1);
                    vinto = checkVittoria(matrix);
                    if (!vinto) {
                        macrocella = -1;
                    }
                } else {
                    newmacrocella = x * 3 + y;
                    if (selezionaMacrocella(ultimateMatrix, newmacrocella)) {
                        macrocella = newmacrocella;
                    }
                }
            } else {
                System.out.println("Posizione già occupata");
            }
        }

        if (vinto) {
            System.out.println("Il giocatore " + getSimboloGiocatore(turno - 1) + " ha vinto!");
        } else {
            System.out.println("La partita è finita in pareggio.");
        }
    }

    /**
     * Controlla se la macrocella è giocabile
     * @param ultimateMatrix
     * @param macrocella
     * @return
     */
    public static boolean selezionaMacrocella(char[][][] ultimateMatrix, int macrocella) {
        if (macrocella < 0 || macrocella > 8) {
            return false;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (ultimateMatrix[macrocella][i][j] == '#') {
                    return true;
                }
            }
        }
        return false;
    }

    public static void gioca(boolean vinto, char[][] matrix, int vers, int turno, int mod, Random rand, Scanner sc, int diff) {
        while (!vinto && turno <= matrix.length * matrix[0].length) {
            int x = -1, y = -1;
            System.out.println("Giocatore " + getSimboloGiocatore(turno));

            if ((turno % 2 == 1 || turno % 2 != 1 && mod == 1) || (turno % 2 == 1 && mod == 2)) {
                x = getValidNumber(sc, "Inserisci la riga:", matrix.length);
                y = getValidNumber(sc, "Inserisci la colonna:", matrix.length);
            } else if (mod == 2 && turno % 2 != 1) {
                if (diff==1) {
                    do {
                        x = rand.nextInt(matrix.length);
                        y = rand.nextInt(matrix.length);
                    } while (matrix[x][y] != '#');
                } else {
                    mossaComputerMedio(matrix);
                }
            }

            if (matrix[x][y] == '#') {
                matrix[x][y] = getSimboloGiocatore(turno);
                turno++;
                printMatrix(matrix);
                vinto = checkVittoriaEstesa(matrix, vers);
            } else {
                System.out.println("Quella posizione è già occupata, scegli un'altra.");
            }
        }

        if (vinto) {
            System.out.println("Il Giocatore " + getSimboloGiocatore(turno - 1) + " ha vinto!");
        } else {
            System.out.println("La partita è finita in pareggio.");
        }
    }

    public static int scegliMod(Scanner sc) {
        int mod;
        do {
            System.out.println("Scegli la modalità");
            System.out.println("1 - Giocatore VS Giocatore");
            System.out.println("2 - Giocatore VS PC");
            mod = sc.nextInt();
            if (mod < 1 || mod > 2) {
                System.out.println("Scelta invalida");
            }
        } while (mod < 1 || mod > 2);
        return mod;
    }

    public static int scegliDimensione(int vers) {
        return switch (vers) {
            case 1, 3 -> 3;
            case 2 -> 5;
            default -> 0;
        };
    }

    public static void spiegaRegole(Scanner sc, int vers) {
        System.out.println("Conosci le regole? S/N");
        char risposta = sc.next().charAt(0);

        if (risposta == 'N' || risposta == 'n') {
            switch (vers) {
                case 1:
                    System.out.println("Le regole sono semplici, si gioca su una griglia di 3x3 caselle. Un giocatore utilizza il simbolo [X], mentre l'altro usa [O]. i giocatori si alternano posizionando il proprio simbolo in una casella vuota con l'obiettivo di allinearne tre in fila, colonna o diagonale per vincere, mentre se tutte le caselle vengono riempite senza un tris, la partita termina in pareggio.");
                    break;
                case 2:
                    System.out.println("Le regole sono semplici, si gioca su una griglia di 5x5 caselle. Un giocatore utilizza il simbolo [X], mentre l'altro usa [O]. i giocatori si alternano posizionando il proprio simbolo in una casella vuota con l'obiettivo di allinearne tre in fila, colonna o diagonale per vincere, mentre se tutte le caselle vengono riempite senza un tris, la partita termina in pareggio.");
                    break;
                case 3:
                    System.out.println("1. Il gioco inizia come un normale TRIS, con un giocatore che usa [X] e l'altro [O]");
                    System.out.println("2. Quando un giocatore piazza il proprio simbolo in una casella di una micro-griglia, l'avversario deve giocare nella macro-cella corrispondente a quella posizione.");
                    System.out.println("3. Se una micro-griglia viene vinta da un giocatore (facendo tris al suo interno), quella macro-cella viene conquistata da quel giocatore.");
                    System.out.println("4. L'obiettivo è vincere l'intera griglia conquistando 3 macro-celle allineate in orizzontale, verticale o diagonale.");
                    System.out.println("5. Se un giocatore deve giocare in una macro-cella già conquistata o piena, può scegliere qualsiasi altra macro-cella ancora disponibile.");
                    break;
            }
        }
    }

    public static int scegliVersione(Scanner sc) {
        int vers;
        do {
            System.out.println("Scegli la versione di TRIS");
            System.out.println("1 - TRIS classico");
            System.out.println("2 - TRIS 5x5");
            System.out.println("3 - Ultimate TRIS");
            vers = sc.nextInt();
            if (vers < 1 || vers > 3) {
                System.out.println("Scelta invalida");
            }
        } while (vers < 1 || vers > 3);
        return vers;
    }

    public static void printMatrix(char[][] matrix) {
        System.out.print("  ");
        for (int i = 0; i < matrix.length; i++) {
            System.out.print((i + 1) + " ");
        }
        System.out.println();

        // Stampa matrice
        for (int i = 0; i < matrix.length; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static boolean checkVittoria(char[][] matrix) {
        // Controlla righe, colonne e diagonali
        for (int i = 0; i < 3; i++) {
            if (matrix[i][0] != '#' && matrix[i][0] == matrix[i][1] && matrix[i][1] == matrix[i][2]) {
                return true;
            }
            if (matrix[0][i] != '#' && matrix[0][i] == matrix[1][i] && matrix[1][i] == matrix[2][i]) {
                return true;
            }
        }

        // Controlla diagonali
        if (matrix[0][0] != '#' && matrix[0][0] == matrix[1][1] && matrix[1][1] == matrix[2][2]) {
            return true;
        }
        if (matrix[0][2] != '#' && matrix[0][2] == matrix[1][1] && matrix[1][1] == matrix[2][0]) {
            return true;
        }

        return false;
    }

    public static void mossaComputerMedio(char[][] matrix) {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matrix[i][j] == '#') {
                    matrix[i][j] = 'O';
                    if (checkVittoria(matrix)) {
                        return; // Se vince, lascia la mossa e termina
                    } else {
                        matrix[i][j] = '#'; // Ripristina la casella
                    }
                }
            }
        }

        // Controllo se il giocatore può vincere e lo blocco
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (matrix[i][j] == '#') {
                    matrix[i][j] = 'X';
                    if (checkVittoria(matrix)) {
                        matrix[i][j] = 'O'; // Blocca il giocatore
                        return;
                    } else {
                        matrix[i][j] = '#'; // Ripristina la casella
                    }
                }
            }
        }

        Random rand = new Random();
        int x = rand.nextInt(3);
        int y = rand.nextInt(3);

        while (matrix[x][y] != '#') {
            x = rand.nextInt(3);
            y = rand.nextInt(3);
        }

        matrix[x][y] = 'O';
    }

    // Nuovo metodo per controllare vittoria in griglia 5x5
    public static boolean checkVittoriaEstesa(char[][] matrix, int vers) {
        if (vers == 1 || vers == 3) {
            return checkVittoria(matrix);
        }

        // Per griglia 5x5, controlla tris in ogni direzione
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == '#') continue;

                // Controllo orizzontale
                if (j <= matrix.length - 3 &&
                        matrix[i][j] == matrix[i][j + 1] &&
                        matrix[i][j] == matrix[i][j + 2]) {
                    return true;
                }

                // Controllo verticale
                if (i <= matrix.length - 3 &&
                        matrix[i][j] == matrix[i + 1][j] &&
                        matrix[i][j] == matrix[i + 2][j]) {
                    return true;
                }

                // Controllo diagonale principale
                if (i <= matrix.length - 3 && j <= matrix.length - 3 &&
                        matrix[i][j] == matrix[i + 1][j + 1] &&
                        matrix[i][j] == matrix[i + 2][j + 2]) {
                    return true;
                }

                // Controllo diagonale secondaria
                if (i <= matrix.length - 3 && j >= 2 &&
                        matrix[i][j] == matrix[i + 1][j - 1] &&
                        matrix[i][j] == matrix[i + 2][j - 2]) {
                    return true;
                }
            }
        }
        return false;
    }

    public static char getSimboloGiocatore(int turno) {
        return (turno % 2 == 1) ? 'X' : 'O';
    }

    public static char[][] generaCampo(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = '#';
            }
        }
        return matrix;
    }

    public static char[][][] generaUltimateMatrix(char[][][] ultimateMatrix) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    ultimateMatrix[i][j][k] = '#';
                }
            }
        }
        return ultimateMatrix;
    }

    public static void printUltimateMatrix(char[][][] ultimateMatrix) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        System.out.print(ultimateMatrix[i * 3 + k][j][l] + " ");
                    }
                    System.out.print("  "); // Separazione tra blocchi
                }
                System.out.println(); // A capo
            }
            System.out.println(); // Separazione tra righe dei blocchi
        }
    }

    public static int getValidNumber(Scanner sc, String message, int maxValue) {
        int num;
        while (true) {
            System.out.println(message);
            if (sc.hasNextInt()) {
                num = sc.nextInt();
                if (num >= 1 && num <= maxValue) {
                    return num - 1; // Convertiamo da 1-maxValue a 0-(maxValue-1)
                } else {
                    System.out.println("Numero fuori intervallo! Inserisci un numero tra 1 e " + maxValue);
                }
            } else {
                System.out.println("Input non valido! Inserisci un numero.");
                sc.next(); // Evita loop infiniti
            }
        }
    }
}