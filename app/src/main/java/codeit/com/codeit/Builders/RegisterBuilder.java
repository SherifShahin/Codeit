package codeit.com.codeit.Builders;

import codeit.com.codeit.Interfaces.UserBuilder;
import codeit.com.codeit.Model.User;

public class RegisterBuilder implements UserBuilder
{
    User user;

    @Override
    public User Build() {
        if(!user.email.isEmpty() && !user.password.isEmpty() &&
                !user.name.isEmpty() && !user.username.isEmpty())
        {
            return user;
        }

        else
            return null;
    }

    @Override
    public UserBuilder reset() {
        user=new User();
        return this;
    }

    @Override
    public UserBuilder setName(String name) {
        user.name=name;
        return this;
    }

    @Override
    public UserBuilder setUserName(String username) {
        user.username=username;
        return this;
    }

    @Override
    public UserBuilder setPassword(String password) {
        user.password=password;
        return this;
    }

    @Override
    public UserBuilder setEmail(String email) {
        user.email=email;
        return this;
    }

    @Override
    public UserBuilder setImgfile(String imgfile) {
        return null;
    }

}
