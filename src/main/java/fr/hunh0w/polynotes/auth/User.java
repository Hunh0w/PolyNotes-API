package fr.hunh0w.polynotes.auth;

import com.mongodb.lang.Nullable;
import fr.hunh0w.polynotes.auth.crypto.CryptoManager;
import fr.hunh0w.polynotes.auth.models.RegisterModel;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Locale;

@MongoEntity(collection = "users")
public class User extends PanacheMongoEntity {

    @BsonProperty("first_name")
    public String firstname;

    @BsonProperty("last_name")
    public String lastname;

    public String email;
    public String password;

    public User(){}

    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname.toLowerCase(Locale.ROOT);
        this.lastname = lastname.toLowerCase(Locale.ROOT);
        this.email = email.toLowerCase(Locale.ROOT);
        this.password = CryptoManager.sha384(password);
    }

    public static User findByRegisterModel(RegisterModel registerModel){
        return find("{ '$or': [ {'$and': [{'firstname': ?1}, {'lastname': ?2}]}, { 'email': ?3 }]}",
                registerModel.firstname.toLowerCase(Locale.ROOT),
                registerModel.lastname.toLowerCase(Locale.ROOT),
                registerModel.email.toLowerCase(Locale.ROOT))
                .firstResult();
    }

    @Nullable
    public static User findByEmail(String email){
        return find("email", email).firstResult();
    }
}
