package ru.practicum.main.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JsonPatch {

    private final ObjectMapper mapper;

    public <T> T mergePatch(T t, T p, Class<T> clazz) {
        try {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            JsonNode original = mapper.valueToTree(t);
            JsonNode patch = mapper.valueToTree(p);
            JsonNode patched = JsonMergePatch.fromJson(patch).apply(original);
            return mapper.treeToValue(patched, clazz);
        } catch (IOException | JsonPatchException e) {
            throw new RuntimeException(e);
        }
    }

}
