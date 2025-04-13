package com.example.petit_reve;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

public class OpenAiService {

    public String getResponse(String prompt) {
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(BuildConfig.OPENAI_API_KEY)
                .build();

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model(ChatModel.GPT_3_5_TURBO)
                .build();

        ChatCompletion completion = client.chat().completions().create(params);
        return completion.choices().get(0).message().content().orElse("Pas de réponse");
    }
}
