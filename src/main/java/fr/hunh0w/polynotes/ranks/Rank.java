package fr.hunh0w.polynotes.ranks;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

@MongoEntity(collection = "ranks")
public class Rank extends PanacheMongoEntity {

    public String name;
    public String color;

    public Rank(){}

    public Rank(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Rank(BuiltinRank builtinRank){
        this(builtinRank.getName(), builtinRank.getColor());
    }

    public static void generateBuiltinRanks(){
        for(BuiltinRank builtinRank : BuiltinRank.values()){
            Rank rank = find("{'name': ?1}", builtinRank.getName()).firstResult();
            if(rank == null) new Rank(builtinRank).persist();
        }
    }

}
