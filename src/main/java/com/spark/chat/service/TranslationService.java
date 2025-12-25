package com.spark.chat.service;

import com.google.cloud.translate.*;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {
    // Note: Ensure your GOOGLE_APPLICATION_CREDENTIALS env var is set!
    private final Translate translate = TranslateOptions.getDefaultInstance().getService();

    public String translate(String text, String from, String to) {
        if (text == null || text.isEmpty() || from.equalsIgnoreCase(to)) {
            return text;
        }

        try {
            Translation translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage(from),
                Translate.TranslateOption.targetLanguage(to)
            );
            return translation.getTranslatedText();
        } catch (Exception e) {
            // Real-world fallback: If API fails, return original text so chat doesn't break
            System.err.println("Translation failed: " + e.getMessage());
            return text; 
        }
    }
}
