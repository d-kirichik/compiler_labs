/**
 * Created by seven-teen on 13.05.17.
 */
 class Token {

     private String domain;
     private String value;
     private Position start, following;

     Token(String domain, String value, Position start, Position following){
         this.domain = domain;
         this.value = value;
         this.start = start;
         this.following = following;
     }

     String getDomain(){
         return this.domain;
     }

     Position getStart(){
         return start;
     }

     String getValue(){return this.value;}

     int calcIndex(){
         switch (this.domain){
             case "NONTERMINAL":
                 return 0;
             case "LP":
                 return 1;
             case "RP":
                 return 2;
             case "TERMINAL":
                 return 3;
             case "TAG":
                 return 4;
             case "$":
                 return 5;
             default:
                 return -1;
         }
     }

     @Override
    public String toString(){
         return this.domain + " at (" + this.start.toString()  + " - " + this.following.toString() + ") " + this.value + ' ';
     }
}
