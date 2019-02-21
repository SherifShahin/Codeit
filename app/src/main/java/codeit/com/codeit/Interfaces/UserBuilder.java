package codeit.com.codeit.Interfaces;

import codeit.com.codeit.Model.User;

public interface UserBuilder
{
    User Build();
    UserBuilder reset();
    UserBuilder setName(String name);
    UserBuilder setUserName(String username);
    UserBuilder setPassword(String password);
    UserBuilder setEmail(String email);
    UserBuilder setImgfile(String imgfile);

}
