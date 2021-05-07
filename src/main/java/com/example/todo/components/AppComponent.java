package com.example.todo.components;

import com.example.todo.entities.UserEntity;
import com.example.todo.requests.SignUpRequest;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.PropertyMap;

@Component
public class AppComponent {

    @Autowired
    PasswordEncoder encoder;

    public ModelMapper signUpModelMapper(String password) {

        ModelMapper modelMapper = new ModelMapper();

        String encoded = encoder.encode(password);

        PropertyMap<SignUpRequest, UserEntity> personMap = new PropertyMap<SignUpRequest, UserEntity>() {
            protected void configure() {
                map().setPassword(encoded);
            }
        };

        modelMapper.addMappings(personMap);

        return modelMapper;

    }
}
