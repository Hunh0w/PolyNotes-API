package fr.hunh0w.polynotes.auth.models;

import io.netty.util.internal.StringUtil;
import io.smallrye.common.constraint.NotNull;
import io.vertx.codegen.annotations.Nullable;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RegisterModel {

    public String firstname;
    public String lastname;
    public String email;
    public String password;

    @Nullable
    public String getError(){
        List<String> emptyFields = getEmptyFields();
        if(!emptyFields.isEmpty()){
            String fields = StringUtil.join(", ", emptyFields).toString();
            return fields+" is empty !";
        }

        return null;
    }

    @NotNull
    private List<String> getEmptyFields() {
        List<String> emptyFields = new ArrayList<>();
        for(Field field : this.getClass().getDeclaredFields()){
            try{
                String attribute = (String)field.get(this);
                if(attribute == null || attribute.isBlank())
                    emptyFields.add(field.getName());
            }catch(IllegalAccessException ex){
                ex.printStackTrace();
            }
        }
        return emptyFields;
    }
}
