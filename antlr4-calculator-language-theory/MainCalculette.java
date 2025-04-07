import java.io.*;
import org.antlr.v4.runtime.*;

// imports pour l'affichage de l'arbre
import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.tree.*; 

public class MainCalculette {
    public static void main(String args[]) throws Exception {
        // Instanciation du lexer
        CalculLexer lex;
        if (args.length == 0) 
            lex = new CalculLexer(CharStreams.fromStream(System.in));
        else 
            lex = new CalculLexer(CharStreams.fromFileName(args[0]));

        // Création du flux de tokens
        CommonTokenStream tokens = new CommonTokenStream(lex);
        
        // Instanciation du parser avec les tokens
        CalculParser parser = new CalculParser(tokens);

        try {
            System.out.println("Code MVàP généré :");
            ParseTree tree = parser.calcul(); // Appel de la règle principale (axiome de la grammaire)
            Trees.inspect(tree, parser); // Affichage graphique de l'arbre syntaxique
        } catch (RecognitionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
