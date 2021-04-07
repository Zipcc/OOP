package DBServer;

/*
* NUL : NULL;
* UNK : UNKNOWN
* EOC : END OF COMMAND ::= ';'
* CT  : <CommandType>  ::=  <Use> | <Create> | <Drop> | <Alter> | <Insert> |
                            <Select> | <Update> | <Delete> | <Join>
* ST  : <Structure>  ::=  DATABASE | TABLE
* WAL : <WildAttribList> ::=  <AttributeList> | *
* LB  :  LEFT BRACE "("
* RB  : RIGHT BRACE ")"
* CM  : COMMA ","
* KW  : INTO | VALUES | FROM | WHERE | SET | AND | ON | ADD
* STR : '<StringLiteral>'
* BOL : <BooleanLiteral>
* FLO : <FloatLiteral>
* INT : <IntegerLiteral>
* OP  : <Operator>  ::=   =   |   >   |   <   |   !   |   LIKE
* ID  : <TableName> | <DatabaseName> | <AttributeName>
*
* Sequence is essential, as in Token(String token) CT,ST,KW,OP should be checked before ID.
* */
public enum TokenType {
    NUL, UNK, EOC, CT, ST, WAL, LB, RB, CM, KW, STR, BOL, FLO, INT, OP, ID;
}
