grammar Calcul;


@parser::members {
    private int _cur_label = 1;
    /** générateur de nom d'étiquettes pour les boucles */
    private String newLabel( ) { return "Label"+(_cur_label++); }; 

    private TablesSymboles tablesSymboles = new TablesSymboles();

    private String transformop(String op) {
        if (op.equals("*")) {
            return "MUL\n";
        } else if (op.equals("/")) {
            return "DIV\n";
        } else if (op.equals("%")) {
            return "MOD\n";
        } else if (op.equals("+")) {
            return "ADD\n";
        } else if (op.equals("-")) {
            return "SUB\n";
        } else {
            System.err.println("Opérateur arithmétique incorrect : " + op);
            throw new IllegalArgumentException("Opérateur arithmétique incorrect : " + op);
        }
    }
}

start : calcul  EOF ;

calcul returns [ String code ]
@init{ $code = new String(); }   // On initialise $code, pour ensuite l'utiliser comme accumulateur
@after{ System.out.println($code); }
    :   (decl { $code += $decl.code; })*
        { $code += "  JUMP Start\n"; }
        NEWLINE*

        (fonction { $code += $fonction.code; })*
        NEWLINE*

        { $code += "LABEL Start\n"; }
        (instruction { $code += $instruction.code; })*

        { $code += "  HALT\n"; }
    ;


instruction returns [ String code ] 
    : expression finInstruction 
        { 
            $code = $expression.code;
        }
    | assignation finInstruction
        { 
            $code = $assignation.code;
        }
    | input finInstruction
        {
            $code = $input.code;
        }
    | output finInstruction
        {
            $code = $output.code;
        }
    | bloc 
        {
            $code = $bloc.code;
        }
    | tantQue 
        {
            $code = $tantQue.code;
        }
    | boucle
        {
            $code = $boucle.code;
        }
    | branchement
        {
            $code = $branchement.code;
        }
    | finInstruction
        {
            $code = "";
        }
    ;

expression returns [ String code, String type ]
    : '-' a=expression 
        { 
            $code = "PUSHI 0\n" + $a.code + "SUB\n"; 
        }
    | a=expression op=('*'|'/'|'%') b=expression 
        { 
            $code = $a.code + $b.code + transformop($op.text); 
        }
    | a=expression op=('+'|'-') b=expression 
        { 
            $code = $a.code + $b.code + transformop($op.text); 
        }
    | '(' e=expression ')' 
        { 
            $code = $e.code; 
        }
    | ENTIER 
        { 
            $code = "PUSHI " + $ENTIER.text + "\n"; 
        }
    |  IDENTIFIANT '(' args ')'  // appel de fonction
        {
            $code = $args.code + "CALL " + $IDENTIFIANT.text + "\n";

            // Nettoyage de la pile des arguments après l'appel
            while($args.size > 0) {
                $code += "POP \n";
                $args.size--;
            }

        }
    | IDENTIFIANT 
        { 
            VariableInfo vi = tablesSymboles.getVar($IDENTIFIANT.text);
            if (vi.scope == VariableInfo.Scope.PARAM) {
                $code = "PUSHL " + vi.address + "\n"; 
            } else {
                $code = "PUSHG " + vi.address + "\n"; 
            }
        }
    ;

decl returns [ String code ]
    : TYPE IDENTIFIANT finInstruction
        {
            $code = "PUSHI 0\n"; 
            tablesSymboles.addVarDecl($IDENTIFIANT.text, $TYPE.text);
        }
    | TYPE IDENTIFIANT '=' expression finInstruction
        {
            $code = $expression.code; 
            tablesSymboles.addVarDecl($IDENTIFIANT.text, $TYPE.text);
        }
    ;

assignation returns [ String code ] 
    : IDENTIFIANT '=' expression
        {
            VariableInfo vi = tablesSymboles.getVar($IDENTIFIANT.text);
            if (vi.scope == VariableInfo.Scope.PARAM) {
                $code = $expression.code + "STOREL " + vi.address + "\n";  // Utilisation de STOREL pour les paramètres
            } else {
                $code = $expression.code + "STOREG " + vi.address + "\n";  // Utilisation de STOREG pour les variables globales
            }
        }
    | IDENTIFIANT '+=' expression
        {  
            VariableInfo vi = tablesSymboles.getVar($IDENTIFIANT.text);
            if (vi.scope == VariableInfo.Scope.PARAM) {
                $code = "PUSHL " + vi.address + "\n" + $expression.code + "ADD\n" + "STOREL " + vi.address + "\n";  // Utilisation de PUSHL et STOREL pour les paramètres
            } else {
                $code = "PUSHG " + vi.address + "\n" + $expression.code + "ADD\n" + "STOREG " + vi.address + "\n";  // Utilisation de PUSHG et STOREG pour les variables globales
            }
        }
    ;

input returns [ String code ]
    : 'input(' IDENTIFIANT ')'
        {
            VariableInfo vi = tablesSymboles.getVar($IDENTIFIANT.text);
            $code = "READ\n" + "STOREG " + vi.address + "\n";            
        }
    ;

output returns [ String code ]
    : 'output(' expression ')'
        {
            $code = $expression.code + "WRITE\n" + "POP\n";            
        }
    ;


bloc returns [ String code ]  @init{ $code = new String(); } 
    : '{' 
            (instruction { $code += $instruction.code; })*    
      '}'  
      NEWLINE*
    ;


tantQue returns [String code]
    : 'while' '(' c=condition ')' i=instruction
        {
            String start = newLabel();
            String end = newLabel();

            $code = "LABEL " + start + "\n" +
                    $c.code +
                    "JUMPF  " + end + "\n" +
                    $i.code +
                    "JUMP " + start + "\n" +
                    "LABEL "+ end + "\n";
        }
    ;


condition returns [String code]
    : 'True'  { $code = "  PUSHI 1\n"; }
    | 'False' { $code = "  PUSHI 0\n"; }
    | a=expression op=('=='|'!='|'<'|'<='|'>'|'>='|'<>') b=expression 
        {
            switch ($op.text) {
                case "==": $code = $a.code + $b.code + "EQUAL\n"; break;
                case "!=": $code = $a.code + $b.code + "NEQ\n"; break;
                case "<>": $code = $a.code + $b.code + "NEQ\n"; break;
                case "<":  $code = $a.code + $b.code + "INF\n"; break;
                case "<=": $code = $a.code + $b.code + "INFEQ\n"; break;
                case ">":  $code = $a.code + $b.code + "SUP\n"; break;
                case ">=": $code = $a.code + $b.code + "SUPEQ\n"; break;
            }
        }
    | 'not' c=condition
        {
            $code = "PUSHI 1\n" + $c.code + "SUB\n";
        }
    | c=condition 'and' d=condition
        {
            $code = $c.code + $d.code + "MUL\n";
        }
    | c=condition 'or' d=condition
        {
            $code = ($c.code + $d.code + "ADD\n")
                  + ($c.code + $d.code + "MUL\n")
                  + "SUB\n";
        }
    ;


branchement returns [String code]
    : 'if' '(' c=condition ')' 'then' blocIf=instruction 'else' blocElse=instruction
        {

            String faux = newLabel(); 
            String end = newLabel();

            $code = $c.code + "JUMPF  " + faux + "\n";
            $code += $blocIf.code + "JUMP " + end + "\n";
            $code += "LABEL " + faux + "\n";
            $code += $blocElse.code ;
            $code += "LABEL "+ end + "\n";

        }
    | 'if' '(' c=condition ')' 'then' blocIf=instruction
        {
            String end = newLabel();

            $code = $c.code + "JUMPF  " + end + "\n";
            $code += $blocIf.code;
            $code += "LABEL " + end + "\n";
        }
    ;

boucle returns [String code]
    : 'for' '(' a=assignation ';' c=condition ';' b=assignation ')' i=instruction
        {
            String start = newLabel();
            String end = newLabel();

            $code = $a.code;
            $code += "LABEL " + start + "\n";
            $code += $c.code + "JUMPF  " + end + "\n";
            $code += $i.code;
            $code += $b.code;
            $code += "JUMP " + start + "\n";
            $code += "LABEL " + end + "\n";
        }
    ;




params
    : TYPE IDENTIFIANT
        {
            tablesSymboles.addParam($IDENTIFIANT.text, $TYPE.text);
        }
        ( ',' TYPE IDENTIFIANT
            {
                tablesSymboles.addParam($IDENTIFIANT.text, $TYPE.text);
            }
        )*
    ;


fonction returns [ String code ]
    @init { tablesSymboles.enterFunction(); } // Activation des tables locales
    @after { tablesSymboles.exitFunction(); } // Désactivation des tables locales
    : TYPE  IDENTIFIANT '('  params ? ')'
        {
            tablesSymboles.addFunction($IDENTIFIANT.text, $TYPE.text);
            $code = "LABEL " + $IDENTIFIANT.text + "\n"; 
        }
        bloc
        {
            $code += $bloc.code;
            $code += "RETURN\n";  //  Return "de sécurité"
        }
    ;

args returns [ String code, int size] @init{ $code = new String(); $size = 0; }
    : ( expression
        {
            $code += $expression.code;
            $size += 1;
        }
    ( ',' expression
        {
            $code += $expression.code;
            $size += 1;
        }
    )*
      )?
    ;


finInstruction : ( ';' | NEWLINE) + ;

TYPE : 'int' | 'double' ;

NEWLINE : '\r'? '\n';

WS : (' ' | '\t')+ -> skip;

ENTIER : ('0'..'9')+;

IDENTIFIANT : [a-zA-Z_][a-zA-Z0-9_]*;

UNMATCH : . { System.err.println("Caractère inattendu : " + getText()); } ;

COMMENT_SL : '//' ~[\r\n]* -> skip ;

COMMENT_ML : '/*' .*? '*/' -> skip ;
