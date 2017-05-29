import java.util.ArrayList;

/**
 * Created by seven-teen on 14.05.17.
 */
class Rule {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rule rule = (Rule) o;

        return members != null ? members.equals(rule.members) : rule.members == null;
    }

    @Override
    public int hashCode() {
        return members != null ? members.hashCode() : 0;
    }

    static class Entry{

        private String value;
        private Boolean isTerm;

        Entry(String value, Boolean isTerm){
            this.value = value;
            this.isTerm = isTerm;
        }

        Entry(Entry e){
            this.value = e.value;
            this.isTerm = e.isTerm;
        }

        String getValue(){
            return value;
        }

        Boolean getIsTerm(){
            return isTerm;
        }

        int calcIndex(){
            switch (value){
                case "Spec":
                    return 0;
                case "Rules":
                    return 1;
                case "Rule":
                    return 2;
                case "AxiomTag":
                    return 3;
                case "Alternatives":
                    return 4;
                case "Alternative":
                    return 5;
                case "Symbols":
                    return 6;
                case "Symbol":
                    return 7;
                default:
                    return -1;
            }
        }

        @Override
        public String toString(){
            return ' ' + this.value + ' ';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entry entry = (Entry) o;

            if (value != null ? !value.equals(entry.value) : entry.value != null) return false;
            return isTerm != null ? isTerm.equals(entry.isTerm) : entry.isTerm == null;
        }

        @Override
        public int hashCode() {
            int result = value != null ? value.hashCode() : 0;
            result = 31 * result + (isTerm != null ? isTerm.hashCode() : 0);
            return result;
        }
    }

    private ArrayList<Entry> members;

    Rule(ArrayList<Rule.Entry> entries){
        this.members = new ArrayList<>(entries);
    }

    Rule (Entry e){
        this.members = new ArrayList<>();
        members.add(e);
    }

    Rule(){
        Entry err = new Entry("err", false);
        members = new ArrayList<>();
        members.add(err);
    }

    ArrayList<Entry> getMembers(){
        return members;
    }

    @Override
    public String toString(){
        String res = " ";
        for(Entry e : members){
            res += e.toString();
        }
        return res;
    }
}
