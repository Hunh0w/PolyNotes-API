package fr.hunh0w.polynotes.ranks;

import com.mongodb.lang.NonNull;

public enum BuiltinRank {

    USER("utilisateur", "#9B9B9B"),
    ADMIN("admin", "#B30000");

    private String name;
    private String color;

    BuiltinRank(String name, String color){
        this.name = name;
        this.color = color;
    }

    @NonNull
    public Rank toRank(){
        Rank rank = Rank.find("{'name': ?1}", name).firstResult();
        if(rank == null){
            rank = new Rank(this);
            rank.persist();
        }
        return rank;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
