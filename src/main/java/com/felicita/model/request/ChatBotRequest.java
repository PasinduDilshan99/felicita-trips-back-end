package com.felicita.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatBotRequest {

    private String name;

    private String email;

    private String phone;

    private Preferences preferences;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Preferences {

        private String flowType;

        private Map<String, List<String>> selections;
    }
}