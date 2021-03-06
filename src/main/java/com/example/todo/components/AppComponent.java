package com.example.todo.components;

import com.example.todo.entities.User;
import com.example.todo.requests.SignUpRequest;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.modelmapper.PropertyMap;

@Component
public class AppComponent {

    @Autowired
    PasswordEncoder encoder;

    public ModelMapper signUpModelMapper(String password) {

        ModelMapper modelMapper = new ModelMapper();

        String encoded = encoder.encode(password);

        PropertyMap<SignUpRequest, User> personMap = new PropertyMap<SignUpRequest, User>() {
            protected void configure() {
                map().setPassword(encoded);
            }
        };

        modelMapper.addMappings(personMap);

        return modelMapper;

    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
