package fr.hunh0w.polynotes.auth;

import com.mongodb.lang.Nullable;
import fr.hunh0w.polynotes.auth.crypto.CryptoManager;
import fr.hunh0w.polynotes.auth.models.RegisterModel;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Locale;

@MongoEntity(collection = "polynotesf_collection")
public class User extends PanacheMongoEntity {

    @BsonProperty("first_name")
    public String firstname;

    @BsonProperty("last_name")
    public String lastname;

    public String email;
    public String password;

    public User(){}

    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = CryptoManager.sha384(password);
    }

    public static User findByRegisterModel(RegisterModel registerModel){
        return find("first_name = ?1 and last_name = ?2 or email = ?3",
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
